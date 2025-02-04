package com.senior25.tzakar.ui.presentation.screen.main.edit_profile

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.firebase.FirebaseAuthRsp
import com.senior25.tzakar.data.local.model.firebase.StatusCode
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.helper.authentication.email.AuthService
import com.senior25.tzakar.helper.authentication.email.AuthServiceImpl
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.encodeToJson
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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


class EditProfileViewModel(
    private val maiRepository: MainRepository
) : CommonViewModel(){

    private val _uiState = MutableStateFlow<EditProfilePageUiState?>(EditProfilePageUiState.Success)
    val uiState: StateFlow<EditProfilePageUiState?> get() = _uiState.asStateFlow()

    private val _popUpState = MutableStateFlow<EditProfilePagePopUp?>(EditProfilePagePopUp.None)
    val popUpState: StateFlow<EditProfilePagePopUp?> get() = _popUpState.asStateFlow()


    var username:String? = ""
    var email:String? = SharedPref.loggedInEmail


    fun onUIEvent(uiEvent: EditProfilePageEvent) = screenModelScope.launch {
        when (uiEvent) {
            is EditProfilePageEvent.UpdateUserName -> username = uiEvent.text
            EditProfilePageEvent.Success -> _uiState.value = EditProfilePageUiState.Success
            EditProfilePageEvent.LoaderView ->  _uiState.value = EditProfilePageUiState.ProgressLoader
            is EditProfilePageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
        }
    }

//    fun updateProfile(): Flow<ApiDataModel<UpdateProfileReq, UpdateProfileRsp>> {
//        _uiState.value = EditProfilePageUiState.ProgressLoader
//        return hitApi(hitApi = {registrationRepository.updateProfile(it)},
//            request = UpdateProfileReq(
//                idUserProfile = AccountsPref.loggedInUserId,
//                firstName = firstName,
//                lastName = lastName,
//                fatherName = fatherName,
//                email = email
//            ),
//            showError = false,
//            emitStatusCode = {
//                _uiState.value = EditProfilePageUiState.Error
//                onResponseErrorMessage.value = it
//            },
//            showLoader =false
//        )
//    }
}

sealed class EditProfilePageEvent {
    data class UpdateUserName(var text:String?):EditProfilePageEvent()
    data object Success: EditProfilePageEvent()
    data object LoaderView: EditProfilePageEvent()
    data class UpdatePopUpState(val popUp: EditProfilePagePopUp) : EditProfilePageEvent()
}

sealed class EditProfilePagePopUp{
    data object None:EditProfilePagePopUp()
    data object SaveChanges:EditProfilePagePopUp()
    data object SaveChangesSuccess:EditProfilePagePopUp()
}


sealed class EditProfilePageUiState {
    data object ProgressLoader : EditProfilePageUiState()
    data object Success : EditProfilePageUiState()
    data object Error : EditProfilePageUiState()
}
