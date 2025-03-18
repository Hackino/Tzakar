package com.senior25.tzakar.ui.presentation.screen.main.notification_history

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class NotificationHistoryViewModel(private val maiRepository: MainRepository): CommonViewModel(){

    private val _uiState = MutableStateFlow<NotificationHistoryPageUiState?>(NotificationHistoryPageUiState.Loading)
    val uiState: StateFlow<NotificationHistoryPageUiState?> get() = _uiState.asStateFlow()

    private val _notifications = MutableStateFlow<List<NotificationModel>?>(null)
    val notifications: StateFlow<List<NotificationModel>?> get() = _notifications.asStateFlow()

    fun init() {
        screenModelScope.launch(Dispatchers.IO) {
            maiRepository.fetchServerNotifications()
            _uiState.value = NotificationHistoryPageUiState.Loading
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