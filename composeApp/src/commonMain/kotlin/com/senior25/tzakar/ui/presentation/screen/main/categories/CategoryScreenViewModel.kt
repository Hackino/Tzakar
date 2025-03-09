package com.senior25.tzakar.ui.presentation.screen.main.categories

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.avatars.AvatarsModel
import com.senior25.tzakar.data.local.model.firebase.FirebaseAuthRsp
import com.senior25.tzakar.data.local.model.gender.GenderModel
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class CategoryViewModel(
    private val maiRepository: MainRepository
) : CommonViewModel(){

    private val _uiState = MutableStateFlow<CategoryPageUiState?>(CategoryPageUiState.Success)
    val uiState: StateFlow<CategoryPageUiState?> get() = _uiState.asStateFlow()

    private val _popUpState = MutableStateFlow<CategoryPagePopUp?>(CategoryPagePopUp.None)
    val popUpState: StateFlow<CategoryPagePopUp?> get() = _popUpState.asStateFlow()

    var title:String? = ""
    var description:String? =""

    private val _reminderDate = MutableStateFlow<String?>(null)
    val reminderDate: StateFlow<String?> get() = _reminderDate.asStateFlow()

    private val _reminderTime = MutableStateFlow<String?>(null)
    val reminderTime: StateFlow<String?> get() = _reminderTime.asStateFlow()


    fun onUIEvent(uiEvent: CategoryPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            CategoryPageEvent.Success -> _uiState.value = CategoryPageUiState.Success
            CategoryPageEvent.LoaderView ->  _uiState.value = CategoryPageUiState.ProgressLoader
            is CategoryPageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
            is CategoryPageEvent.UpdateTitle -> { title = uiEvent.title }
            is CategoryPageEvent.UpdateDescription -> { description = uiEvent.description }
            is CategoryPageEvent.UpdateReminderDate -> {
                _reminderDate.value = uiEvent.date
            }
            is CategoryPageEvent.UpdateReminderTime -> {
                _reminderTime.value = uiEvent.time

            }
        }
    }

    fun setCategory(type:Int? = null,onSuccess:(AuthResult?)->Unit) {
        screenModelScope.launch {
            _uiState.value = CategoryPageUiState.ProgressLoader
//            email?.let {
//                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference)
//                    .child(it.encodeBase64())
//                val userJson = ref.valueEvents.first().value
//                if (userJson != null) {
//                    val user = userJson.toString().decodeJson(UserProfile())
//
//                    val updatedUser = user?.copy(
//                        userName = username,
//                        genderId = _selectedGender.value,
//                        image =_image.value
//                    )
//
//                    ref.setValue(updatedUser.encodeToJson())
//                    SharedPref.loggedInProfile = updatedUser
//                    onSuccess(FirebaseAuthRsp().authResult)
//                    return@launch
//                }
//            }
        }
    }
}

sealed class CategoryPageEvent {
    data object Success: CategoryPageEvent()
    data object LoaderView: CategoryPageEvent()
    data class UpdatePopUpState(val popUp: CategoryPagePopUp) : CategoryPageEvent()
    data class UpdateTitle(val title: String?) : CategoryPageEvent()
    data class UpdateDescription(val description: String?) : CategoryPageEvent()
    data class UpdateReminderDate(val date: String?) : CategoryPageEvent()
    data class UpdateReminderTime(val time: String?) : CategoryPageEvent()
}

sealed class CategoryPagePopUp{
    data object None:CategoryPagePopUp()
    data object SaveChanges:CategoryPagePopUp()
    data object SaveChangesSuccess:CategoryPagePopUp()
    data object SelectAValidDateBefore:CategoryPagePopUp()

}


sealed class CategoryPageUiState {
    data object ProgressLoader : CategoryPageUiState()
    data object Success : CategoryPageUiState()
    data object Error : CategoryPageUiState()
}
