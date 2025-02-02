package com.senior25.tzakar.ui.presentation.screen.registration.forget_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){

    private val _uiState = MutableStateFlow<ForgotPasswordPageUiState?>(ForgotPasswordPageUiState.Success)
    val uiState: StateFlow<ForgotPasswordPageUiState?> get() = _uiState.asStateFlow()

    var email:String? = null
    var password:String? = null

    fun onUIEvent(uiEvent: ForgotPasswordPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            is ForgotPasswordPageEvent.UpdateEmail -> email = uiEvent.email
            ForgotPasswordPageEvent.Success -> _uiState.value = ForgotPasswordPageUiState.Success
            ForgotPasswordPageEvent.LoaderView ->  _uiState.value = ForgotPasswordPageUiState.ProgressLoader
        }
    }

}

sealed class ForgotPasswordPageEvent {
    data class UpdateEmail(var email:String?):ForgotPasswordPageEvent()
    data object Success:ForgotPasswordPageEvent()
    data object LoaderView:ForgotPasswordPageEvent()
}

sealed class ForgotPasswordPageUiState {
    data object ProgressLoader : ForgotPasswordPageUiState()
    data object Success : ForgotPasswordPageUiState()
    data object Error : ForgotPasswordPageUiState()

}
