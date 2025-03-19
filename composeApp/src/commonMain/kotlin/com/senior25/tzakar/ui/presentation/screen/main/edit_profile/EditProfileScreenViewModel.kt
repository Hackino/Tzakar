package com.senior25.tzakar.ui.presentation.screen.main.edit_profile

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.avatars.AvatarsModel
import com.senior25.tzakar.data.local.model.firebase.FirebaseAuthRsp
import com.senior25.tzakar.data.local.model.gender.GenderModel
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class EditProfileViewModel(
    private val maiRepository: MainRepository
) : CommonViewModel(){

    private val _uiState = MutableStateFlow<EditProfilePageUiState?>(EditProfilePageUiState.Success)
    val uiState: StateFlow<EditProfilePageUiState?> get() = _uiState.asStateFlow()

    private val _popUpState = MutableStateFlow<EditProfilePagePopUp?>(EditProfilePagePopUp.None)
    val popUpState: StateFlow<EditProfilePagePopUp?> get() = _popUpState.asStateFlow()

    private val _image = MutableStateFlow(SharedPref.loggedInProfile?.image)
    val image: StateFlow<String?> get() = _image.asStateFlow()

    private val _selectedGender = MutableStateFlow(SharedPref.loggedInProfile?.genderId?:-1)
    val selectedGender: StateFlow<Int> get() = _selectedGender.asStateFlow()

    var username:String? = SharedPref.loggedInProfile?.userName?:""

    var email:String? = SharedPref.loggedInEmail

    var avatars: AvatarsModel? = null

    init {
        screenModelScope.launch(Dispatchers.IO) {
            val ref = Firebase.database.reference(DataBaseReference.Avatars.reference)
            val snapshot = ref.valueEvents.firstOrNull()
            avatars = snapshot?.value<AvatarsModel?>()
        }
    }

    fun onUIEvent(uiEvent: EditProfilePageEvent) = screenModelScope.launch {
        when (uiEvent) {
            is EditProfilePageEvent.UpdateUserName -> username = uiEvent.text
            EditProfilePageEvent.Success -> _uiState.value = EditProfilePageUiState.Success
            EditProfilePageEvent.LoaderView ->  _uiState.value = EditProfilePageUiState.ProgressLoader
            is EditProfilePageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
            is EditProfilePageEvent.UpdateSelectedGender ->{
                _selectedGender.value = uiEvent.gender?.id?:-1
            }
            is EditProfilePageEvent.UpdateImage -> {
                _image.value = uiEvent.image
            }
        }
    }

    fun updateProfile(onSuccess:(AuthResult?)->Unit) {
        screenModelScope.launch {
            _uiState.value = EditProfilePageUiState.ProgressLoader
            email?.let {
                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference)
                    .child(it.encodeBase64()).child("profile")

                val snapshot = ref.valueEvents.firstOrNull()
                val profile  = snapshot?.value<UserProfile?>()
                if (profile != null) {
                    val updatedUser = profile.copy(
                        userName = username,
                        genderId = _selectedGender.value,
                        image =_image.value
                    )
                    ref.setValue(updatedUser)
                    SharedPref.loggedInProfile = updatedUser
                    onSuccess(FirebaseAuthRsp().authResult)
                    return@launch
                }
            }
        }
    }
}

sealed class EditProfilePageEvent {
    data class UpdateUserName(var text:String?):EditProfilePageEvent()
    data class UpdateSelectedGender(var gender:GenderModel?):EditProfilePageEvent()
    data class UpdateImage(var image:String?):EditProfilePageEvent()
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
