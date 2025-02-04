package com.senior25.tzakar.ui.presentation.screen.main.profile

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfilePageEvent
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfilePagePopUp
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfilePageUiState
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.ProfilePageData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val maiRepository: MainRepository): CommonViewModel(){

    private val _uiState = MutableStateFlow<ProfilePageUiState?>(ProfilePageUiState.Loading)
    val uiState: StateFlow<ProfilePageUiState?> get() = _uiState.asStateFlow()


    private val _popUpState = MutableStateFlow<ProfilePagePopUp?>(ProfilePagePopUp.None)
    val popUpState: StateFlow<ProfilePagePopUp?> get() = _popUpState.asStateFlow()


    private var _profilePageData = MutableStateFlow<ProfilePageData?>(ProfilePageData())

    fun  init() {
        screenModelScope.launch(Dispatchers.IO) {
//            hitApi(
//                hitApi ={ registrationRepository.getProfile(it)},
//                request = GetProfileReq(),
//                showLoader = false,
//                showError = false,
//                emitStatusCode = {
//                    _uiState.value = ProfilePageUiState.Error(_profilePageData.value)
//                }
//            ).collectLatest {
//                val newData = ProfilePageData.updatePageData()
//                updateState(ProfilePageUiState.Success(newData))
//            }
        }
    }

    fun onUIEvent(uiEvent: ProfilePageEvent) = screenModelScope.launch {
        when (uiEvent) {
            ProfilePageEvent.Success -> _uiState.value = ProfilePageUiState.Success(_profilePageData.value)
            ProfilePageEvent.LoaderView ->  _uiState.value = ProfilePageUiState.ProgressLoader(_profilePageData.value)
            is ProfilePageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
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
}

sealed class ProfilePageUiState(open val data: ProfilePageData?) {
    data object Loading : ProfilePageUiState(null)
    data class Success(override val data: ProfilePageData?) : ProfilePageUiState(data)
    data class ProgressLoader(override val data: ProfilePageData?) : ProfilePageUiState(data)

    data class Error(override val data: ProfilePageData?) : ProfilePageUiState(data)
}

sealed class ProfilePagePopUp{
    data object None:ProfilePagePopUp()
    data object Logout:ProfilePagePopUp()
}
