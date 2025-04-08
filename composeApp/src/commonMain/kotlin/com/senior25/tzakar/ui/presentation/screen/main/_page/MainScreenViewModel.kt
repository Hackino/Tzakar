package com.senior25.tzakar.ui.presentation.screen.main._page

import cafe.adriel.voyager.core.model.screenModelScope
import com.adrianwitaszak.kmmpermissions.permissions.model.Permission
import com.adrianwitaszak.kmmpermissions.permissions.service.PermissionsService
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainScreenViewModel(
    private val registrationRepository: RegistrationRepository
): CommonViewModel(), KoinComponent {

    private val permissionsService: PermissionsService by inject()

    private val _popUpState = MutableStateFlow<MainPagePopUp?>(MainPagePopUp.None)
    val popUpState: StateFlow<MainPagePopUp?> get() = _popUpState.asStateFlow()

    private val _userProfile = MutableStateFlow(SharedPref.loggedInProfile)
    val userProfile: StateFlow<UserProfile?> get() = _userProfile.asStateFlow()

    private val _longLat = MutableStateFlow<List<Double>?>(emptyList())
    val longLat: StateFlow<List<Double>?> get() = _longLat.asStateFlow()

    private val _permissions = MutableStateFlow(defaultPermission())
    private val permissions: StateFlow<HashMap<Permission, Boolean>> get() = _permissions.asStateFlow()

    private fun defaultPermission()= hashMapOf<Permission, Boolean>().apply {
        this[Permission.LOCATION_FOREGROUND] = false
        this[Permission.LOCATION_BACKGROUND] = false
        this[Permission.LOCATION_SERVICE_ON] = false
        this[Permission.POST_NOTIFICATION] = false
    }

    fun init(){
        screenModelScope.launch {
            permissionsService.checkPermissionFlow(Permission.LOCATION_FOREGROUND).collectLatest{
                _permissions.value[Permission.LOCATION_FOREGROUND] = it.isGranted()
            }

            permissionsService.checkPermissionFlow(Permission.LOCATION_BACKGROUND).collectLatest{
                _permissions.value[Permission.LOCATION_BACKGROUND] = it.isGranted()
            }

            permissionsService.checkPermissionFlow(Permission.LOCATION_SERVICE_ON).collectLatest{
                _permissions.value[Permission.LOCATION_SERVICE_ON] = it.isGranted()
            }

            permissionsService.checkPermissionFlow(Permission.POST_NOTIFICATION).collectLatest{
                _permissions.value[Permission.POST_NOTIFICATION] = it.isGranted()
            }

            permissions.collectLatest {
                if (it[Permission.LOCATION_FOREGROUND] == false){
                    permissionsService.providePermission(Permission.LOCATION_FOREGROUND)
                    return@collectLatest
                }

                if (it[Permission.LOCATION_BACKGROUND] == false){
                    permissionsService.providePermission(Permission.LOCATION_BACKGROUND)
                    return@collectLatest
                }

                if (it[Permission.LOCATION_SERVICE_ON] == false){
                    permissionsService.providePermission(Permission.LOCATION_SERVICE_ON)
                    return@collectLatest
                }

                if (it[Permission.POST_NOTIFICATION] == false){
                    permissionsService.providePermission(Permission.POST_NOTIFICATION)
                    return@collectLatest
                }
            }
        }
        fetchProfile()
    }

    fun fetchProfile() {
        screenModelScope.launch(Dispatchers.IO) {
            SharedPref.loggedInEmail?.let {
                val ref = Firebase.database.reference(DataBaseReference.UserProfiles.reference).child(it.encodeBase64()).child("profile")
                val snapshot = ref.valueEvents.firstOrNull()
                val profile  = snapshot?.value<UserProfile?>()
                _userProfile.value =  profile?:SharedPref.loggedInProfile
                SharedPref.loggedInProfile = _userProfile.value
                println(profile)
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

            is MainPageEvent.UpdateProfile -> _userProfile.value = SharedPref.loggedInProfile

            MainPageEvent.Refresh -> fetchProfile()

            is MainPageEvent.UpdateLongLat -> _longLat.value = uiEvent.longLat

        }
    }

    fun getCurrentDate() = getCurrentDateFormatted()
}

sealed class MainPageEvent {
    data class UpdatePopUpState(val popUp: MainPagePopUp) : MainPageEvent()
    data object UpdateProfile : MainPageEvent()
    data object Refresh : MainPageEvent()
    data class UpdateLongLat(val longLat:List<Double>) : MainPageEvent()

}

sealed class MainPagePopUp{
    data object None:MainPagePopUp()
    data object LoginPopUp:MainPagePopUp()
    data object CategoriesSheet:MainPagePopUp()
}