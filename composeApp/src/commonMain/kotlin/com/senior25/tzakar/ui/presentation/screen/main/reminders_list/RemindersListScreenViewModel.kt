package com.senior25.tzakar.ui.presentation.screen.main.reminders_list

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.TriggerType
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

class RemindersListScreenViewModel(
    private val mainRepository: MainRepository
): CommonViewModel(){

    private val _uiState = MutableStateFlow<RemindersListPageUiState?>(null)
    val uiState: StateFlow<RemindersListPageUiState?> get() = _uiState.asStateFlow()

    private val _tabIndexState = MutableStateFlow<ReminderTabType?>(ReminderTabType.CURRENT)
    val tabIndexState: StateFlow<ReminderTabType?> get() = _tabIndexState.asStateFlow()

    private val _popUpState = MutableStateFlow<RemindersListPagePopUp?>(RemindersListPagePopUp.None)
    val popUpState: StateFlow<RemindersListPagePopUp?> get() = _popUpState.asStateFlow()


    private val _reminders = MutableStateFlow<List<ReminderModel>?>(emptyList())
    val reminders: StateFlow<List<ReminderModel>?> get() = _reminders.asStateFlow()

    private val _filteredReminders = MutableStateFlow<Map<String, List<ReminderModel>>?>(null)
    val filteredReminders: StateFlow<Map<String, List<ReminderModel>>?> get() = _filteredReminders.asStateFlow()

    fun init() {
        screenModelScope.launch {
            screenModelScope.launch(Dispatchers.IO) { mainRepository.fetchServerReminder() }
            screenModelScope.launch {
                mainRepository.getAllReminders().collectLatest { reminder(it?.filterNotNull()) }
            }
            screenModelScope.launch { reminders.collectLatest { filterData(it) } }
        }
    }

    fun reminder(reminders:List<ReminderModel>?){
        val allReminders = reminders?.filter { it.triggerType == TriggerType.TIME.value }?.onEach{
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

        _reminders.value = allReminders
    }

    fun onUIEvent(uiEvent: RemindersListPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            RemindersListPageEvent.Success -> _uiState.value = RemindersListPageUiState.Success
            RemindersListPageEvent.LoaderView ->  _uiState.value = RemindersListPageUiState.ProgressLoader
            is RemindersListPageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
            RemindersListPageEvent.Refresh ->{
                updateState(RemindersListPageUiState.Refreshing)
                screenModelScope.launch(Dispatchers.IO) { mainRepository.fetchServerReminder() }
                screenModelScope.launch {
                    val reminders = mainRepository.getAllRemindersFromDb()
                    reminder(reminders?.filterNotNull())
                }
                delay(2000)
                updateState(RemindersListPageUiState.Success)
            }
            is RemindersListPageEvent.LoadCurrent -> {
                _tabIndexState.value = ReminderTabType.CURRENT
                filterData(_reminders.value)
            }
            is RemindersListPageEvent.LoadExpired -> {
                _tabIndexState.value  = ReminderTabType.COMPLETED
                filterData(_reminders.value)
            }
            RemindersListPageEvent.Init -> init()
            is RemindersListPageEvent.UpdateReminderStatus -> {
                uiEvent.reminderModel?.let { mainRepository.updateReminder(it) }
            }
        }
    }

    private fun filterData(list:List<ReminderModel>?){
        _filteredReminders.value =list?.filter { if (_tabIndexState.value == ReminderTabType.COMPLETED) it.isCompleted == true else it.isCompleted !=true}
            ?.filter { it.date != null && it.time != null }
            ?.sortedWith(
                compareBy(
                    { it.date?.let { it1 -> LocalDate.parse(it1) } },
                    { it.time?.let { it1 -> LocalTime.parse(it1) } },
                )
            ).let {
                if (_tabIndexState.value == ReminderTabType.COMPLETED) it?.reversed() else it
            }?.groupBy { it.date!! }
    }


    private fun updateState(newState: RemindersListPageUiState) {
        if (_uiState.value != newState) _uiState.value = newState
    }
}

sealed class RemindersListPageEvent {
    data object Success: RemindersListPageEvent()
    data object LoaderView: RemindersListPageEvent()
    data object Refresh: RemindersListPageEvent()
    data class UpdatePopUpState(val popUp: RemindersListPagePopUp) : RemindersListPageEvent()
    data object LoadCurrent : RemindersListPageEvent()
    data object LoadExpired : RemindersListPageEvent()
    data object Init : RemindersListPageEvent()
    data class UpdateReminderStatus(val reminderModel: ReminderModel?): RemindersListPageEvent()


}

sealed class RemindersListPageUiState(open val data: RemindersListPageData?) {
    data object Loading : RemindersListPageUiState(null)
    data object Refreshing : RemindersListPageUiState(null)
    data object Success : RemindersListPageUiState(null)
    data object ProgressLoader : RemindersListPageUiState(null)
    data object Error : RemindersListPageUiState(null)
}

sealed class RemindersListPagePopUp{
    data object None:RemindersListPagePopUp()
}

enum class ReminderTabType(val value:Int){
    CURRENT(0),   COMPLETED(1),
}