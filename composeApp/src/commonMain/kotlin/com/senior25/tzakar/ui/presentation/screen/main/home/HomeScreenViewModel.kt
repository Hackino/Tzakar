package com.senior25.tzakar.ui.presentation.screen.main.home

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HomeScreenViewModel(
    private val mainRepository: MainRepository
): CommonViewModel(){

    private val _uiState = MutableStateFlow<HomePageUiState?>(null)
    val uiState: StateFlow<HomePageUiState?> get() = _uiState.asStateFlow()

    private val _tabIndexState = MutableStateFlow<ReminderTabType?>(ReminderTabType.CURRENT)
    val tabIndexState: StateFlow<ReminderTabType?> get() = _tabIndexState.asStateFlow()

    private val _todayReminderCount = MutableStateFlow(0)
    val todayReminderCount: StateFlow<Int> get() = _todayReminderCount.asStateFlow()

    private val _totalReminderCount = MutableStateFlow(0)
    val totalReminderCount: StateFlow<Int> get() = _totalReminderCount.asStateFlow()

    private val _todayCompletedReminderCount = MutableStateFlow<Int>(0)
    val todayCompletedReminderCount: StateFlow<Int> get() = _todayCompletedReminderCount.asStateFlow()

    private val _totalCompleteReminderCount = MutableStateFlow<Int>(0)
    val totalCompleteReminderCount: StateFlow<Int> get() = _totalCompleteReminderCount.asStateFlow()

    private val _popUpState = MutableStateFlow<HomePagePopUp?>(HomePagePopUp.None)
    val popUpState: StateFlow<HomePagePopUp?> get() = _popUpState.asStateFlow()

    private var _homePageData = MutableStateFlow<HomePageData?>(HomePageData())

    private val _reminders = MutableStateFlow<List<ReminderModel>?>(emptyList())
    val reminders: StateFlow<List<ReminderModel>?> get() = _reminders.asStateFlow()

    private val _filteredReminders = MutableStateFlow<List<ReminderModel>?>(emptyList())
    val filteredReminders: StateFlow<List<ReminderModel>?> get() = _filteredReminders.asStateFlow()

    fun init() {
        screenModelScope.launch {
            screenModelScope.launch(Dispatchers.IO) { mainRepository.fetchServerReminder() }
            screenModelScope.launch {
                mainRepository.getAllReminders().collectLatest { reminder(it) }
            }
            screenModelScope.launch { reminders.collectLatest { filterData(it) } }
        }
    }

    fun reminder(reminders:List<ReminderModel>?){
        val todayDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val allReminders = reminders?.onEach{
            val latestDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
            it.date?.let { dateStr ->
                val parsedDate = LocalDate.parse(dateStr)
                if (parsedDate < latestDate)it.isCompleted = true
                else if(parsedDate == latestDate){
                    it.time?.let { timeStr ->
                        val parsedTime = LocalTime.parse(timeStr)
                        if (parsedTime <= currentTime)it.isCompleted = true
                    }
                }
            }
        }?.toList()

        _totalReminderCount.value = allReminders?.size?:0
        _totalCompleteReminderCount.value =allReminders?.filter { it.isCompleted == true }?.size?:0
        _reminders.value = allReminders?.filter {   it.date?.let { LocalDate.parse(it) == todayDate }?:false }
        _todayReminderCount.value = _reminders.value?.size?:0
        _todayCompletedReminderCount.value =_reminders.value?.filter { it.isCompleted == true }?.size?:0
    }

    fun onUIEvent(uiEvent: HomePageEvent) = screenModelScope.launch {
        when (uiEvent) {
            HomePageEvent.Success -> _uiState.value = HomePageUiState.Success
            HomePageEvent.LoaderView ->  _uiState.value = HomePageUiState.ProgressLoader
            is HomePageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
            HomePageEvent.Refresh ->{
                updateState(HomePageUiState.Refreshing)
                screenModelScope.launch(Dispatchers.IO) { mainRepository.fetchServerReminder() }
                screenModelScope.launch {
                    val reminders = mainRepository.getAllRemindersFromDb()
                    reminder(reminders)
                }
                delay(2000)
                updateState(HomePageUiState.Success)
            }
            is HomePageEvent.LoadCurrent -> {
                _tabIndexState.value = ReminderTabType.CURRENT
                filterData(_reminders.value)
            }
            is HomePageEvent.LoadExpired -> {
                _tabIndexState.value  = ReminderTabType.COMPLETED
                filterData(_reminders.value)
            }
            HomePageEvent.Init -> init()
            is HomePageEvent.UpdateReminderStatus -> {
                uiEvent.reminderModel?.let { mainRepository.updateReminder(it) }
            }
        }
    }

    private fun filterData(list:List<ReminderModel>?){
        _filteredReminders.value =list?.filter { if (_tabIndexState.value == ReminderTabType.COMPLETED) it.isCompleted == true else it.isCompleted !=true}
    }


    private fun updateState(newState: HomePageUiState) {
        if (_uiState.value != newState) {
            _homePageData.value =newState.data
            _uiState.value = newState
        }
    }
}

sealed class HomePageEvent {
    data object Success: HomePageEvent()
    data object LoaderView: HomePageEvent()
    data object Refresh: HomePageEvent()
    data class UpdatePopUpState(val popUp: HomePagePopUp) : HomePageEvent()
    data object LoadCurrent : HomePageEvent()
    data object LoadExpired : HomePageEvent()
    data object Init : HomePageEvent()
    data class UpdateReminderStatus(val reminderModel: ReminderModel?): HomePageEvent()


}

sealed class HomePageUiState(open val data: HomePageData?) {
    data object Loading : HomePageUiState(null)
    data object Refreshing : HomePageUiState(null)
    data object Success : HomePageUiState(null)
    data object ProgressLoader : HomePageUiState(null)
    data object Error : HomePageUiState(null)
}

sealed class HomePagePopUp{
    data object None:HomePagePopUp()
}

enum class ReminderTabType(val value:Int){
    CURRENT(0),   COMPLETED(1),
}