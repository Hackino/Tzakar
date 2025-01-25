package com.senior25.tzakar.ui.presentation.screen.registration.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senior25.tzakar.domain.RegistrationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignInScreenViewModel(
    private val registrationRepository: RegistrationRepository
): ViewModel(){

    private val _uiState = MutableStateFlow<SignInPageUiState?>(SignInPageUiState.Success)
    val uiState: StateFlow<SignInPageUiState?> get() = _uiState.asStateFlow()

    var email:String? = null
    var password:String? = null
    var isRememberMe:Boolean? = null

    fun onUIEvent(uiEvent: SignInPageEvent) = viewModelScope.launch {
        when (uiEvent) {
            is SignInPageEvent.UpdateEmail -> email = uiEvent.email
            is SignInPageEvent.UpdatePassword -> password = uiEvent.password
            is SignInPageEvent.UpdateRememberMe -> isRememberMe = uiEvent.isRememberMe
            SignInPageEvent.Success -> _uiState.value = SignInPageUiState.Success
            SignInPageEvent.LoaderView ->  _uiState.value = SignInPageUiState.ProgressLoader
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

sealed class SignInPageEvent {
    data class UpdateEmail(var email:String?):SignInPageEvent()
    data class UpdateRememberMe(var isRememberMe:Boolean?):SignInPageEvent()

    data class UpdatePassword(var password:String?):SignInPageEvent()
    data object Success:SignInPageEvent()
    data object LoaderView:SignInPageEvent()

}

sealed class SignInPageUiState {
    data object ProgressLoader : SignInPageUiState()
    data object Success : SignInPageUiState()
    data object Error : SignInPageUiState()

}
