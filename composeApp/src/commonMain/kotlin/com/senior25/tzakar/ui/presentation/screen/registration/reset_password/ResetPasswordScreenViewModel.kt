package com.senior25.tzakar.ui.presentation.screen.registration.reset_password

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){

    private val _uiState = MutableStateFlow<ResetPasswordPageUiState?>(ResetPasswordPageUiState.Success)
    val uiState: StateFlow<ResetPasswordPageUiState?> get() = _uiState.asStateFlow()


    val _dialogType = MutableStateFlow<ResetPasswordActionDialogType?>(null)
    val dialogType: StateFlow<ResetPasswordActionDialogType?> get() = _dialogType.asStateFlow()

    private val _password = MutableStateFlow("Test@123")
    val password: StateFlow<String> get() = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("Test@123")
    val confirmPassword: StateFlow<String> get() = _confirmPassword.asStateFlow()

    fun onUIEvent(uiEvent: ResetPasswordPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            is ResetPasswordPageEvent.UpdatePassword -> _password.value = uiEvent.password?:""
            is ResetPasswordPageEvent.UpdateConfirmPassword -> _confirmPassword.value = uiEvent.confirmPassword?:""
            ResetPasswordPageEvent.Success -> _uiState.value = ResetPasswordPageUiState.Success
            ResetPasswordPageEvent.LoaderView ->  _uiState.value = ResetPasswordPageUiState.ProgressLoader
        }
    }

    fun resetPassword(email:String?,onSuccess:()->Unit) {
        screenModelScope.launch{
            _isLoading.update { true }
            email?.let {
                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(it.encodeBase64()).child("profile")

                val snapshot = ref.valueEvents.firstOrNull()
                val profile  = snapshot?.value<UserProfile?>()
                if (profile != null) {
                    ref.setValue(profile?.copy(
                        password = _password.value
                    ))
                    _isLoading.update { null }
                    onSuccess()
                    return@launch
                }
            }
            _isLoading.update { null }
        }
    }
}

sealed class ResetPasswordPageEvent {
    data class UpdatePassword(var password:String?):ResetPasswordPageEvent()
    data class UpdateConfirmPassword(var confirmPassword:String?):ResetPasswordPageEvent()
    data object Success:ResetPasswordPageEvent()
    data object LoaderView:ResetPasswordPageEvent()
}

sealed class ResetPasswordPageUiState {
    data object ProgressLoader : ResetPasswordPageUiState()
    data object Success : ResetPasswordPageUiState()
    data object Error : ResetPasswordPageUiState()
}