package com.senior25.tzakar.ui.presentation.screen.registration.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senior25.tzakar.domain.RegistrationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignUpScreenViewModel(
    private val registrationRepository: RegistrationRepository
): ViewModel(){

    private val _uiState = MutableStateFlow<SignUpPageUiState?>(SignUpPageUiState.Success)
    val uiState: StateFlow<SignUpPageUiState?> get() = _uiState.asStateFlow()

    var email:String? = null
    var password:String? = null
    var username:String? = null

    fun onUIEvent(uiEvent: SignUpPageEvent) = viewModelScope.launch {
        when (uiEvent) {
            is SignUpPageEvent.UpdateEmail -> email = uiEvent.email
            is SignUpPageEvent.UpdateUsername -> username = uiEvent.username
            is SignUpPageEvent.UpdatePassword -> password = uiEvent.password
            SignUpPageEvent.Success -> _uiState.value = SignUpPageUiState.Success
            SignUpPageEvent.LoaderView ->  _uiState.value = SignUpPageUiState.ProgressLoader
        }
    }

//    fun signUp(): Flow<ApiDataModel<SignUpReq, SignUpRsp>> {
//        _uiState.value = SignUpPageUiState.ProgressLoader
//        return hitApi(hitApi = {registrationRepository.signUP(it)},
//            request = SignUpReq(msisdn = phoneNumber),
//            showError = false,
//            emitStatusCode = {
//                _uiState.value =  SignUpPageUiState.Error
//                onResponseErrorMessage.value = it
//            },
//            showLoader =false
//        )
//    }
}

sealed class SignUpPageEvent {
    data class UpdateEmail(var email:String?):SignUpPageEvent()
    data class UpdateUsername(var username:String?):SignUpPageEvent()
    data class UpdatePassword(var password:String?):SignUpPageEvent()
    data object Success:SignUpPageEvent()
    data object LoaderView:SignUpPageEvent()

}

sealed class SignUpPageUiState {
    data object ProgressLoader : SignUpPageUiState()
    data object Success : SignUpPageUiState()
    data object Error : SignUpPageUiState()

}
