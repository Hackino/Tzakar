package com.senior25.tzakar.ui.presentation.screen.main.edit_profile

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.firebase.FirebaseAuthRsp
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.encodeToJson
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


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

    fun updateProfile(onSuccess:(AuthResult?)->Unit) {
        screenModelScope.launch {
            _uiState.value = EditProfilePageUiState.ProgressLoader
            email?.let {
                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference)
                    .child(it.encodeBase64())
                val userJson = ref.valueEvents.first().value
                if (userJson != null) {
                    val user = userJson.toString().decodeJson(UserProfile())
                    ref.setValue(user?.copy(
                        userName = username,
                    ).encodeToJson())
                    SharedPref.selectedLanguage
                    onSuccess(FirebaseAuthRsp().authResult)
                    return@launch
                }
            }
        }
    }
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
