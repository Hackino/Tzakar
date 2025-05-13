package com.senior25.tzakar.ui.presentation.screen.main.notification_history

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.notification.NotificationModel
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class NotificationHistoryViewModel(private val maiRepository: MainRepository): CommonViewModel(){

    private val _uiState = MutableStateFlow<NotificationHistoryPageUiState?>(NotificationHistoryPageUiState.Loading)
    val uiState: StateFlow<NotificationHistoryPageUiState?> get() = _uiState.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationModel>?>(null)
    val notifications: StateFlow<List<NotificationModel>?> get() = _notifications.asStateFlow()

    private var isInit: Boolean  = false

    fun init() {
        screenModelScope.launch {
            if (!isInit) {
                screenModelScope.launch(Dispatchers.IO) { maiRepository.fetchServerNotifications() }
                _uiState.value = NotificationHistoryPageUiState.Loading
                isInit = true
            }
            maiRepository.getAllNotifications().collectLatest { notifications(it?.filterNotNull()) }
        }
    }

    fun onUIEvent(uiEvent: NotificationHistoryPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            NotificationHistoryPageEvent.Success -> _uiState.value = NotificationHistoryPageUiState.Success
            NotificationHistoryPageEvent.LoaderView ->  _uiState.value = NotificationHistoryPageUiState.ProgressLoader
            NotificationHistoryPageEvent.Refresh ->{
                _uiState.value = NotificationHistoryPageUiState.Refreshing
                screenModelScope.launch(Dispatchers.IO) { maiRepository.fetchServerNotifications() }
                val notifications = maiRepository.getAllNotificationsFromDb()
                delay(2000)
                notifications(notifications?.filterNotNull())
            }
        }
    }

    fun notifications(notifications:List<NotificationModel>?){
        val sorted =  notifications?.sortedWith(compareBy(
            { it.date?.let { it1 -> LocalDate.parse(it1) } },
            { it.time?.let { it1 -> LocalTime.parse(it1) } },
        ))
        _notifications.value = sorted?.reversed()
        _uiState.value = NotificationHistoryPageUiState.Success
    }
}

sealed class NotificationHistoryPageEvent {
    data object Success: NotificationHistoryPageEvent()
    data object LoaderView: NotificationHistoryPageEvent()
    data object Refresh : NotificationHistoryPageEvent()
}

sealed class NotificationHistoryPageUiState() {
    data object Loading : NotificationHistoryPageUiState()
    data object Success : NotificationHistoryPageUiState()
    data object Refreshing : NotificationHistoryPageUiState()
    data object ProgressLoader : NotificationHistoryPageUiState()
}