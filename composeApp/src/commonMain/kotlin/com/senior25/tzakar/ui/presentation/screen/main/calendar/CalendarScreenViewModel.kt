package com.senior25.tzakar.ui.presentation.screen.main.calendar

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.menu.MenuModel
import com.senior25.tzakar.data.local.model.reminder.ReminderModel
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.ui.presentation.bottom_sheet.categories.getSortingFilter
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class CalendarViewModel(
    private val maiRepository: MainRepository
) : CommonViewModel(){

    private val _popUpState = MutableStateFlow<CalendarPagePopUp?>(CalendarPagePopUp.None)
    val popUpState: StateFlow<CalendarPagePopUp?> get() = _popUpState.asStateFlow()

    private var _selectedFilters = MutableStateFlow<MutableList<MenuModel>?>(mutableListOf())
    val selectedFilters: StateFlow<MutableList<MenuModel>?> get() = _selectedFilters.asStateFlow()

    private var _selectedSorting = MutableStateFlow<MutableList<MenuModel>?>(
        mutableListOf(getSortingFilter().first())
    )

    val selectedSorting: StateFlow<MutableList<MenuModel>?> get() = _selectedSorting.asStateFlow()

    private val _uiState = MutableStateFlow<CalendarPageUiState?>(CalendarPageUiState.Success)
    val uiState: StateFlow<CalendarPageUiState?> get() = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow<Int?>(null)
    val selectedDate: StateFlow<Int?> get() = _selectedDate.asStateFlow()

    private val _selectedYear = MutableStateFlow<Int?>(null)
    val selectedYear: StateFlow<Int?> get() = _selectedYear.asStateFlow()

    private val _selectedMonth = MutableStateFlow<Month?>(null)
    val selectedMonth: StateFlow<Month?> get() = _selectedMonth.asStateFlow()

    private val _monthDates = MutableStateFlow<List<Pair<Int,String>>?>(null)
    val monthDates: StateFlow<List<Pair<Int,String>>?> get() = _monthDates.asStateFlow()

    private val _reminders = MutableStateFlow<List<ReminderModel>?>(emptyList())
    val reminders: StateFlow<List<ReminderModel>?> get() = _reminders.asStateFlow()

    private val _filteredReminders = MutableStateFlow<List<ReminderModel>?>(emptyList())
    val filteredReminders: StateFlow<List<ReminderModel>?> get() = _filteredReminders.asStateFlow()

    private val _shouldAutoScroll = MutableStateFlow<Boolean?>(false)
    val shouldAutoScroll: StateFlow<Boolean?> get() = _shouldAutoScroll.asStateFlow()

    fun init() {
        screenModelScope.launch {
            screenModelScope.launch(Dispatchers.IO) {
                maiRepository.fetchServerReminder()
            }
            val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            _selectedYear.value = currentDate.year
            _selectedMonth.value = currentDate.month
            _selectedDate.value = currentDate.dayOfMonth
            _monthDates.value = getDaysInMonthWithWeekdays(currentDate.year, currentDate.month)
            requestScroll(true)
            screenModelScope.launch {
                maiRepository.getAllReminders().collectLatest {
                    _reminders.value = it?.toList()
                }
            }

            screenModelScope.launch {
                reminders.collectLatest { filterData(it) }
            }
        }
    }

    private fun getDaysInMonthWithWeekdays(year: Int, month: Month): List<Pair<Int, String>> {
        val firstDayOfMonth = LocalDate(year, month, 1)
        val lastDayOfMonth = firstDayOfMonth
            .plus(1, DateTimeUnit.MONTH)
            .minus(1, DateTimeUnit.DAY)
        val numberOfDays = lastDayOfMonth.dayOfMonth
        return (1..numberOfDays).map { day ->
            val currentDay = firstDayOfMonth.plus(day - 1, DateTimeUnit.DAY)
            val dayOfWeek = currentDay.dayOfWeek.name
            Pair(day, dayOfWeek)
        }
    }

    fun onUIEvent(uiEvent: CalendarPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            CalendarPageEvent.Success -> _uiState.value = CalendarPageUiState.Success
            CalendarPageEvent.LoaderView ->  _uiState.value = CalendarPageUiState.ProgressLoader
            is CalendarPageEvent.UpdateMonthYear ->{
                screenModelScope.launch(Dispatchers.IO) {
                    maiRepository.fetchServerReminder()
                }
                _selectedYear.value = uiEvent.year
                _selectedMonth.value = uiEvent.month
                _selectedDate.value = 1
                _monthDates.value =getDaysInMonthWithWeekdays(uiEvent.year, uiEvent.month)
                requestScroll(true)
                filterData(_reminders.value)

            }
            is CalendarPageEvent.UpdateDayDate -> {
                screenModelScope.launch(Dispatchers.IO) { maiRepository.fetchServerReminder() }
                _selectedDate.value = uiEvent.dayDate
                filterData(_reminders.value)
            }

            is CalendarPageEvent.RemoveAutoScroll -> requestScroll(false)
            CalendarPageEvent.Init -> { init() }
            is CalendarPageEvent.UpdatePopUpState ->_popUpState.value = uiEvent.popUp
            is CalendarPageEvent.UpdateReminderStatus ->{
                uiEvent.reminderModel?.let { maiRepository.updateReminder(it) }
            }
        }
    }

    private fun filterData(list:List<ReminderModel>?){

        val types = _selectedFilters.value?.map { it.id } ?: emptyList()

        val month =((_selectedMonth.value?.ordinal?:0)+1).toString()

        val currentDate = "${_selectedYear.value}-${if (month.length==1) ("0$month") else month }-${_selectedDate.value}"

        val filterByDate = list?.filter { currentDate == it.date }

        val filterByCategory = if (types.isNotEmpty()) {
            filterByDate?.filter { types.contains(it.type) }
        } else {
            filterByDate
        }

        val sorted = filterByCategory?.sortedWith(
            compareBy({ it.time?.let { it1 -> LocalTime.parse(it1) } },)
        )

        val finale = when (selectedSorting.value?.first()?.id) {
            1 -> sorted
            2 -> sorted?.reversed()
            else -> sorted
        }
        _filteredReminders.value = finale
    }

    private fun requestScroll(scroll: Boolean) {
        screenModelScope.launch { _shouldAutoScroll.emit(scroll) }
    }

    fun updateFilter(filters:List<MenuModel>,sorting:List<MenuModel>) {
        _selectedFilters.value = filters.toMutableList()
        _selectedSorting.value = sorting.toMutableList()
        filterData(_reminders.value)
    }

    fun resetFilters() {
        _selectedFilters.value = mutableListOf()
        _selectedSorting.value = mutableListOf(getSortingFilter().first())
        filterData(_reminders.value)
    }

}

sealed class CalendarPagePopUp{
    data object None:CalendarPagePopUp()
    data object ShowCategoriesFilterSheet:CalendarPagePopUp()
}

sealed class CalendarPageEvent {
    data object Success: CalendarPageEvent()
    data object LoaderView: CalendarPageEvent()
    data class UpdateMonthYear(val month:Month, val year:Int): CalendarPageEvent()
    data class UpdateReminderStatus(val reminderModel: ReminderModel?): CalendarPageEvent()

    data class UpdateDayDate( val dayDate:Int): CalendarPageEvent()
    data object Init: CalendarPageEvent()
    data class UpdatePopUpState(val popUp: CalendarPagePopUp) : CalendarPageEvent()
    data object RemoveAutoScroll : CalendarPageEvent()
}

sealed class CalendarPageUiState {
    data object ProgressLoader : CalendarPageUiState()
    data object Success : CalendarPageUiState()
    data object Error : CalendarPageUiState()
}