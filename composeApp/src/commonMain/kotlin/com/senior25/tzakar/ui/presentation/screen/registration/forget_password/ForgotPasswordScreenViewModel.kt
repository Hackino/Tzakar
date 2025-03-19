package com.senior25.tzakar.ui.presentation.screen.registration.forget_password

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.firebase.StatusCode
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.invalid_email_address
import tzakar_reminder.composeapp.generated.resources.user_doesnt_exist

class ForgotPasswordScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){

    private val _uiState = MutableStateFlow<ForgotPasswordPageUiState?>(ForgotPasswordPageUiState.Success)
    val uiState: StateFlow<ForgotPasswordPageUiState?> get() = _uiState.asStateFlow()

    var email:String? = "ramsiskhortoum1@gmail.com"

    fun checkEmail(email:String?,proceed:()->Unit){
        screenModelScope.launch {
            _isLoading.update { true }
            email?.encodeBase64()?.let {
                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(it).child("profile")
                val exist =ref.valueEvents.first().value != null
                if (exist) proceed()
                else{
                    _errorStatusCode.update {
                        StatusCode(errorMessage = getString(Res.string.user_doesnt_exist))
                    }
                }
            }
            _isLoading.update { null }
        }
    }

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
