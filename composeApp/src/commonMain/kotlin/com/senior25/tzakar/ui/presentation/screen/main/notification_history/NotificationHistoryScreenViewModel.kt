package com.senior25.tzakar.ui.presentation.screen.main.notification_history

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import com.senior25.tzakar.ui.presentation.screen.main.notification_history.NotificationHistoryPageData.Companion.checkIfNotContainData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationHistoryViewModel(private val maiRepository: MainRepository): CommonViewModel(){

    private val _uiState = MutableStateFlow<NotificationHistoryPageUiState?>(null)
    val uiState: StateFlow<NotificationHistoryPageUiState?> get() = _uiState.asStateFlow()

    private val _notificationState = MutableStateFlow<Boolean?>(SharedPref.notificationPermissionStatus.value == 1)
    val notificationState: StateFlow<Boolean?> get() = _notificationState.asStateFlow()

    private var _NotificationHistoryPageData = MutableStateFlow<NotificationHistoryPageData?>(NotificationHistoryPageData())

    fun init() {
        updateState(NotificationHistoryPageUiState.Loading(_NotificationHistoryPageData.value))
        screenModelScope.launch(Dispatchers.IO) {
            if(isDataFound(_NotificationHistoryPageData.value)) updateState(NotificationHistoryPageUiState.Success(_NotificationHistoryPageData.value))

//            SharedPref.loggedInEmail?.let {
//                val ref = Firebase.database.reference(DataBaseReference.UserNotificationHistorys.reference).child(it.encodeBase64())
//                val userJson = ref.valueEvents.first().value
//                if (userJson != null) {
//                    val user =  userJson.toString().decodeJson(UserNotificationHistory())
//                    _NotificationHistoryPageData.value = _NotificationHistoryPageData.value?.updatePageData(user)
//                    updateState(NotificationHistoryPageUiState.Success(_NotificationHistoryPageData.value))
//                    return@launch
//                }
//            }
        }
    }

    fun onUIEvent(uiEvent: NotificationHistoryPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            NotificationHistoryPageEvent.Success -> _uiState.value = NotificationHistoryPageUiState.Success(_NotificationHistoryPageData.value)
            NotificationHistoryPageEvent.LoaderView ->  _uiState.value = NotificationHistoryPageUiState.ProgressLoader(_NotificationHistoryPageData.value)
            NotificationHistoryPageEvent.Refresh -> {}
        }
    }

    private fun isDataFound(data: NotificationHistoryPageData?):Boolean {
        if (data.checkIfNotContainData()){
            updateState(NotificationHistoryPageUiState.NoData(data))
            return false
        }
        return true
    }

    private fun updateState(newState: NotificationHistoryPageUiState) {
        if (_uiState.value != newState) {
            _NotificationHistoryPageData.value =newState.data
            _uiState.value = newState
        }
    }
}

sealed class NotificationHistoryPageEvent {
    data object Success: NotificationHistoryPageEvent()
    data object LoaderView: NotificationHistoryPageEvent()
    data object Refresh : NotificationHistoryPageEvent()

}

sealed class NotificationHistoryPageUiState(open val data: NotificationHistoryPageData?) {
    data class Loading(override val data: NotificationHistoryPageData?) : NotificationHistoryPageUiState(data)
    data class Success(override val data: NotificationHistoryPageData?) : NotificationHistoryPageUiState(data)
    data class ProgressLoader(override val data: NotificationHistoryPageData?) : NotificationHistoryPageUiState(data)
    data class Error(override val data: NotificationHistoryPageData?) : NotificationHistoryPageUiState(data)
    data class NoData(override val data: NotificationHistoryPageData?) : NotificationHistoryPageUiState(data)
    data class Refreshing(override val data: NotificationHistoryPageData?) : NotificationHistoryPageUiState(data)
}
