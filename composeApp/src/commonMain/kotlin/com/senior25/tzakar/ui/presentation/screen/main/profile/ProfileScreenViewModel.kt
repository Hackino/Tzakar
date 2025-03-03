package com.senior25.tzakar.ui.presentation.screen.main.profile

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.firebase.FirebaseAuthRsp
import com.senior25.tzakar.data.local.model.firebase.StatusCode
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.NotificationStatus
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.ProfilePageData
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.ProfilePageData.Companion.updatePageData
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel(private val maiRepository: MainRepository): CommonViewModel(){

    private val _uiState = MutableStateFlow<ProfilePageUiState?>(null)
    val uiState: StateFlow<ProfilePageUiState?> get() = _uiState.asStateFlow()

    private val _popUpState = MutableStateFlow<ProfilePagePopUp?>(ProfilePagePopUp.None)
    val popUpState: StateFlow<ProfilePagePopUp?> get() = _popUpState.asStateFlow()

    private val _notificationState = MutableStateFlow<Boolean?>(SharedPref.notificationPermissionStatus.value == 1)
    val notificationState: StateFlow<Boolean?> get() = _notificationState.asStateFlow()

    private var _profilePageData = MutableStateFlow<ProfilePageData?>(ProfilePageData())

    fun init() {
        updateState(ProfilePageUiState.Loading(_profilePageData.value))
        screenModelScope.launch(Dispatchers.IO) {
            SharedPref.loggedInEmail?.let {
                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(it.encodeBase64())
                val userJson = ref.valueEvents.first().value
                if (userJson != null) {
                    val user =  userJson.toString().decodeJson(UserProfile())
                    _profilePageData.value = _profilePageData.value?.updatePageData(user)
                    updateState(ProfilePageUiState.Success(_profilePageData.value))
                    return@launch
                }
            }
        }
    }

    fun deleteAccount(onSuccess:()->Unit) {
        screenModelScope.launch {
            SharedPref.loggedInEmail?.let {
                Firebase.database.reference(DataBaseReference.UserProfiles.reference)
                    .child(it.encodeBase64()).removeValue()
                onSuccess()
                return@launch
            }
        }
    }
    fun onUIEvent(uiEvent: ProfilePageEvent) = screenModelScope.launch {
        when (uiEvent) {
            ProfilePageEvent.Success -> _uiState.value = ProfilePageUiState.Success(_profilePageData.value)
            ProfilePageEvent.LoaderView ->  _uiState.value = ProfilePageUiState.ProgressLoader(_profilePageData.value)
            is ProfilePageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
            is ProfilePageEvent.UpdateNotificationState -> {
                SharedPref.notificationPermissionStatus = if (uiEvent.isChecked) NotificationStatus.ON else NotificationStatus.OFF
                _notificationState.value =  SharedPref.notificationPermissionStatus.value == 1
            }
        }
    }

    private fun updateState(newState: ProfilePageUiState) {
        if (_uiState.value != newState) {
            _profilePageData.value =newState.data
            _uiState.value = newState
        }
    }
}

sealed class ProfilePageEvent {
    data object Success: ProfilePageEvent()
    data object LoaderView: ProfilePageEvent()
    data class UpdatePopUpState(val popUp: ProfilePagePopUp) : ProfilePageEvent()
    data class UpdateNotificationState(val isChecked: Boolean) : ProfilePageEvent()

}

sealed class ProfilePageUiState(open val data: ProfilePageData?) {
    data class Loading(override val data: ProfilePageData?) : ProfilePageUiState(data)
    data class Success(override val data: ProfilePageData?) : ProfilePageUiState(data)
    data class ProgressLoader(override val data: ProfilePageData?) : ProfilePageUiState(data)

    data class Error(override val data: ProfilePageData?) : ProfilePageUiState(data)
}

sealed class ProfilePagePopUp{
    data object None:ProfilePagePopUp()
    data object Logout:ProfilePagePopUp()
    data object DeleteAccount:ProfilePagePopUp()

}
