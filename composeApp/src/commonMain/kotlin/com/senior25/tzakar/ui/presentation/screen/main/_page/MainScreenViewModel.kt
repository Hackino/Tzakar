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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
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
    private val permissions: StateFlow<HashMap<Permission, PermissionStatus>> get() = _permissions.asStateFlow()

    private val permissionMutex = Mutex()

    private fun defaultPermission()= hashMapOf<Permission, PermissionStatus>().apply {
//        this[Permission.LOCATION_FOREGROUND] = PermissionStatus.UNKNOWN
//        this[Permission.LOCATION_BACKGROUND] = PermissionStatus.UNKNOWN
//        this[Permission.LOCATION_SERVICE_ON] = PermissionStatus.UNKNOWN
        this[Permission.POST_NOTIFICATION] = PermissionStatus.UNKNOWN
    }

    private val permissionsToTrack = listOf(
//        Permission.LOCATION_FOREGROUND,
//        Permission.LOCATION_BACKGROUND,
//        Permission.LOCATION_SERVICE_ON,
        Permission.POST_NOTIFICATION
    )

    fun init(){
//        screenModelScope.launch {
//            fetchPermissions()
//            screenModelScope.launch(Dispatchers.Main) {
//                permissions.collectLatest {map ->
//                    permissionMutex.withLock {
//                        when {
////                            map[Permission.LOCATION_FOREGROUND] != PermissionStatus.ON -> {
////                                permissionsService.providePermission(Permission.LOCATION_FOREGROUND){
////                                    fetchPermissions()
////                                }
////                            }
////                            map[Permission.LOCATION_BACKGROUND] != PermissionStatus.ON -> {
////                                permissionsService.providePermission(Permission.LOCATION_BACKGROUND){
////                                    fetchPermissions()
////                                }
////                            }
////                            map[Permission.LOCATION_SERVICE_ON]  != PermissionStatus.ON  -> {
////                                permissionsService.providePermission(Permission.LOCATION_SERVICE_ON){
////                                    fetchPermissions()
////                                }
////                            }
//                            map[Permission.POST_NOTIFICATION] != PermissionStatus.ON  -> {
//                                permissionsService.providePermission(Permission.POST_NOTIFICATION){
//                                    fetchPermissions()
//
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
        fetchProfile()
    }

    private fun fetchPermissions() {
        screenModelScope.launch {
            val updatedMap = _permissions.value.toMutableMap()
            permissionsToTrack.forEach { permission ->
                val result = permissionsService.checkPermission(permission)
                val status = PermissionStatus.getByValue(result.isGranted())
                if (updatedMap[permission] != status) {
                    updatedMap[permission] = status
                    _permissions.value = HashMap(updatedMap)
                }
            }
        }
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

enum class PermissionStatus(val value: Boolean?) {
    UNKNOWN(null),OFF(false), ON(true);
    companion object {
        private val VALUES = entries.toTypedArray()
        fun getByValue(value: Boolean) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
    }
}