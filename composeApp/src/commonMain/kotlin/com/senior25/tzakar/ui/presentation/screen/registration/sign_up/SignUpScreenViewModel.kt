package com.senior25.tzakar.ui.presentation.screen.registration.sign_up

import androidx.lifecycle.viewModelScope
import com.senior25.tzakar.data.local.model.StatusCode
import com.senior25.tzakar.data.local.model.User
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.helper.authentication.email.AuthService
import com.senior25.tzakar.helper.authentication.email.AuthServiceImpl
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.email_already_exist
import tzakar_reminder.composeapp.generated.resources.invalid_credentials
import tzakar_reminder.composeapp.generated.resources.invalid_user
import tzakar_reminder.composeapp.generated.resources.something_went_wrong
import tzakar_reminder.composeapp.generated.resources.too_many_request
import tzakar_reminder.composeapp.generated.resources.weak_password

class SignUpScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){

    private val _uiState = MutableStateFlow<SignUpPageUiState?>(SignUpPageUiState.Success)
    val uiState: StateFlow<SignUpPageUiState?> get() = _uiState.asStateFlow()

    var email:String? = null
    var password:String? = null
    var username:String? = null

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private var authService: AuthService? = null

    init {
        authService =  AuthServiceImpl(auth = Firebase.auth)
        launchWithCatchingException {
            authService?.currentUser?.collect {
                _currentUser.value = it
            }
        }
    }

    fun onUIEvent(uiEvent: SignUpPageEvent) = viewModelScope.launch {
        when (uiEvent) {
            is SignUpPageEvent.UpdateEmail -> email = uiEvent.email
            is SignUpPageEvent.UpdateUsername -> username = uiEvent.username
            is SignUpPageEvent.UpdatePassword -> password = uiEvent.password
            SignUpPageEvent.Success -> _uiState.value = SignUpPageUiState.Success
            SignUpPageEvent.LoaderView ->  _uiState.value = SignUpPageUiState.ProgressLoader
        }
    }

    fun createUser(onSuccess:(AuthResult)->Unit) {
        viewModelScope.launch{
            _isLoading.update { true }
            val result =authService?.createUser(email?:"", password?:"")
            if (result?.authResult == null){
                val errorText =   when(AuthServiceImpl.FirebaseException.getByValue(result?.statusCode?.code)){
                    AuthServiceImpl.FirebaseException.FirebaseAuthActionCodeException ->{
                        Res.string.something_went_wrong
                    }
                    AuthServiceImpl.FirebaseException.FirebaseAuthEmailException -> {
                        Res.string.something_went_wrong
                    }
                    AuthServiceImpl.FirebaseException.FirebaseAuthInvalidCredentialsException -> {
                        Res.string.invalid_credentials
                    }
                    AuthServiceImpl.FirebaseException.FirebaseAuthWeakPasswordException -> {
                        Res.string.weak_password
                    }
                    AuthServiceImpl.FirebaseException.FirebaseAuthInvalidUserException -> {
                        Res.string.invalid_user
                    }
                    AuthServiceImpl.FirebaseException.FirebaseAuthMultiFactorException -> {
                        Res.string.something_went_wrong
                    }
                    AuthServiceImpl.FirebaseException.FirebaseAuthRecentLoginRequiredException -> {
                        Res.string.something_went_wrong
                    }
                    AuthServiceImpl.FirebaseException.FirebaseAuthUserCollisionException -> {
                        Res.string.email_already_exist
                    }
                    AuthServiceImpl.FirebaseException.FirebaseAuthWebException ->{
                        Res.string.something_went_wrong
                    }
                    AuthServiceImpl.FirebaseException.Unknown -> {
                        Res.string.something_went_wrong
                    }

                    AuthServiceImpl.FirebaseException.FirebaseTooManyRequestsException -> {
                        Res.string.too_many_request
                    }
                }
                _errorStatusCode.update { StatusCode(errorMessage = getString(errorText)) }
            }else{
                onSuccess(result.authResult)
            }
            _isLoading.update { null }
        }
    }

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
