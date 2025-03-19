package com.senior25.tzakar.ui.presentation.screen.main._page

import cafe.adriel.voyager.core.model.screenModelScope
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.NotificationStatus
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.RegistrationRepository
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.platform_specific.common.getCurrentDateFormatted
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(){

    private val _popUpState = MutableStateFlow<MainPagePopUp?>(MainPagePopUp.None)
    val popUpState: StateFlow<MainPagePopUp?> get() = _popUpState.asStateFlow()

    private val _userProfile = MutableStateFlow(SharedPref.loggedInProfile)
    val userProfile: StateFlow<UserProfile?> get() = _userProfile.asStateFlow()

    fun init(){ fetchProfile() }

    fun fetchProfile() {
        screenModelScope.launch(Dispatchers.IO) {
            SharedPref.loggedInEmail?.let {
                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(it.encodeBase64()).child("profile")
                val snapshot = ref.valueEvents.firstOrNull()
                val profile  = snapshot?.value<UserProfile?>()
                _userProfile.value  =  profile?:SharedPref.loggedInProfile
                SharedPref.loggedInProfile = _userProfile.value
                return@launch
            }
        }
    }

    fun updateNotificationStatus(result:Boolean){
        SharedPref.notificationPermissionStatus =if (result) NotificationStatus.ON else NotificationStatus.OFF
    }

    fun onUIEvent(uiEvent: MainPageEvent) = screenModelScope.launch {
        when (uiEvent) {
            is MainPageEvent.UpdatePopUpState -> _popUpState.value = uiEvent.popUp
            is MainPageEvent.UpdateProfile ->{
                _userProfile.value = SharedPref.loggedInProfile
            }
        }
    }

    fun getCurrentDate() = getCurrentDateFormatted()
}

sealed class MainPageEvent {
    data class UpdatePopUpState(val popUp: MainPagePopUp) : MainPageEvent()
    data object UpdateProfile : MainPageEvent()
}

sealed class MainPagePopUp{
    data object None:MainPagePopUp()
    data object LoginPopUp:MainPagePopUp()
    data object CategoriesSheet:MainPagePopUp()
}