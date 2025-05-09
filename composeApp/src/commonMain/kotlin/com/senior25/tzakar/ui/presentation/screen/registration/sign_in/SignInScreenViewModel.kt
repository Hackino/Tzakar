package com.senior25.tzakar.ui.presentation.screen.registration.sign_in

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.firebase.FirebaseAuthRsp
import com.senior25.tzakar.data.local.model.firebase.FirebaseUser
import com.senior25.tzakar.data.local.model.firebase.StatusCode
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.helper.authentication.email.AuthService
import com.senior25.tzakar.helper.authentication.email.AuthServiceImpl
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.invalid_credentials
import tzakar_reminder.composeapp.generated.resources.invalid_user
import tzakar_reminder.composeapp.generated.resources.something_went_wrong
import tzakar_reminder.composeapp.generated.resources.too_many_request
import tzakar_reminder.composeapp.generated.resources.weak_password

class SignInScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){

    private val _uiState = MutableStateFlow<SignInPageUiState?>(SignInPageUiState.Success)
    val uiState: StateFlow<SignInPageUiState?> get() = _uiState.asStateFlow()

    var email:String? = "ramsiskhortoum1@gmail.com"
    var password:String? = "Test@123"
    var isRememberMe:Boolean? = SharedPref.isRememberMeChecked

    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    val currentUser = _currentUser.asStateFlow()

    private var authService: AuthService? = null

    init {
        authService =  AuthServiceImpl(auth = Firebase.auth)
    }

    fun onUIEvent(uiEvent: SignInPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            is SignInPageEvent.UpdateEmail -> email = uiEvent.email
            is SignInPageEvent.UpdatePassword -> password = uiEvent.password
            is SignInPageEvent.UpdateRememberMe -> isRememberMe = uiEvent.isRememberMe
            SignInPageEvent.Success -> _uiState.value = SignInPageUiState.Success
            SignInPageEvent.LoaderView ->  _uiState.value = SignInPageUiState.ProgressLoader
        }
    }

    fun onSignInClick(onSuccess:(AuthResult?)->Unit) {
        screenModelScope.launch{
            email?.let {
                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(it.encodeBase64()).child("profile")

                val snapshot = ref.valueEvents.firstOrNull()
                val profile  = snapshot?.value<UserProfile?>()

                if (profile != null) {
                    if (profile.password != password){
                        _errorStatusCode.update { StatusCode(errorMessage = getString(Res.string.invalid_credentials)) }
                    }else{
                        SharedPref.loggedInProfile = profile
                        SharedPref.isRememberMeChecked = isRememberMe == true
                        onSuccess(FirebaseAuthRsp().authResult)


                    }
                    return@launch
                }
            }
            _isLoading.update { true }
            val result =authService?.authenticate(email?:"", password?:"")
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
                        Res.string.something_went_wrong
                    }
                    AuthServiceImpl.FirebaseException.FirebaseAuthWebException ->{
                        Res.string.something_went_wrong
                    }
                    AuthServiceImpl.FirebaseException.Unknown ->  Res.string.something_went_wrong

                    AuthServiceImpl.FirebaseException.FirebaseTooManyRequestsException -> {
                        Res.string.too_many_request
                    }
                }
                _errorStatusCode.update { StatusCode(errorMessage = getString(errorText)) }
            }else{
                CoroutineScope(Dispatchers.Main).launch{
                    email?.let {
                        val ref = Firebase.database
                            .reference(DataBaseReference.UserProfiles.reference)
                            .child(it.encodeBase64()).child("profile")

                        val snapshot = ref.valueEvents.firstOrNull()
                        val profile  = snapshot?.value<UserProfile?>()
                       val updatedUser =  profile?.copy(
                            email = email,
                            password = password
                        )
                        ref.setValue(updatedUser)
                        SharedPref.loggedInProfile = updatedUser
                        SharedPref.isRememberMeChecked = isRememberMe == true
                        onSuccess(result.authResult)
                    }
                }
            }
            _isLoading.update { null }
        }
    }

    fun onSignOut() {
        launchWithCatchingException {
            authService?.signOut()
        }
    }
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