package com.senior25.tzakar.ui.presentation.screen.main.change_password

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.firebase.FirebaseAuthRsp
import com.senior25.tzakar.data.local.model.firebase.StatusCode
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.encodeToJson
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
import tzakar_reminder.composeapp.generated.resources.incorrect_old_password

class ChangePasswordScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){

    private val _uiState = MutableStateFlow<ChangePasswordPageUiState?>(ChangePasswordPageUiState.Success)
    val uiState: StateFlow<ChangePasswordPageUiState?> get() = _uiState.asStateFlow()

    val _dialogType = MutableStateFlow<ChangePasswordActionDialogType?>(null)
    val dialogType: StateFlow<ChangePasswordActionDialogType?> get() = _dialogType.asStateFlow()

    private val _oldPassword = MutableStateFlow("Test@123")
    val oldPassword: StateFlow<String> get() = _oldPassword.asStateFlow()

    private val _newPassword = MutableStateFlow("Test@123")
    val newPassword: StateFlow<String> get() = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("Test@123")
    val confirmPassword: StateFlow<String> get() = _confirmPassword.asStateFlow()

    fun onUIEvent(uiEvent: ChangePasswordPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            is ChangePasswordPageEvent.UpdateOldPassword -> _oldPassword.value = uiEvent.password?:""
            is ChangePasswordPageEvent.UpdateNewPassword -> _newPassword.value = uiEvent.password?:""
            is ChangePasswordPageEvent.UpdateConfirmPassword -> _confirmPassword.value = uiEvent.confirmPassword?:""
            ChangePasswordPageEvent.Success -> _uiState.value = ChangePasswordPageUiState.Success
            ChangePasswordPageEvent.LoaderView ->  _uiState.value = ChangePasswordPageUiState.ProgressLoader
        }
    }

    fun changePassword(onSuccess:()->Unit) {
        screenModelScope.launch{
            _isLoading.update { true }
            SharedPref.loggedInEmail?.let {
                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(it.encodeBase64())
                val userJson = ref.valueEvents.first().value
                if (userJson != null) {
                    val user =  userJson.toString().decodeJson(UserProfile())
                    if (user?.password != oldPassword.value.trim()){
                        _errorStatusCode.update { StatusCode(errorMessage = getString(Res.string.incorrect_old_password)) }
                    }else{
                        ref.setValue(user?.copy(password = _newPassword.value).encodeToJson())
                        onSuccess()
                    }
                    _isLoading.update { null }
                    return@launch
                }
            }
            _isLoading.update { null }
        }
    }
}

sealed class ChangePasswordPageEvent {
    data class UpdateOldPassword(var password:String?):ChangePasswordPageEvent()
    data class UpdateNewPassword(var password:String?):ChangePasswordPageEvent()
    data class UpdateConfirmPassword(var confirmPassword:String?):ChangePasswordPageEvent()
    data object Success:ChangePasswordPageEvent()
    data object LoaderView:ChangePasswordPageEvent()
}

sealed class ChangePasswordPageUiState {
    data object ProgressLoader : ChangePasswordPageUiState()
    data object Success : ChangePasswordPageUiState()
    data object Error : ChangePasswordPageUiState()
}