package com.senior25.tzakar.ui.presentation.screen.main.notification_history

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
        screenModelScope.launch(Dispatchers.IO) {
            if (!isInit) {
                maiRepository.fetchServerNotifications()
                _uiState.value = NotificationHistoryPageUiState.Loading
                isInit = true
            }
            maiRepository.getAllNotifications().collectLatest {

                val sorted =  it.sortedWith(compareBy(
                    { it.date?.let { it1 -> LocalDate.parse(it1) } },
                    { it.time?.let { it1 -> LocalTime.parse(it1) } },
                ))
                _uiState.value = NotificationHistoryPageUiState.Success
                _notifications.value = sorted
            }
        }
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
}