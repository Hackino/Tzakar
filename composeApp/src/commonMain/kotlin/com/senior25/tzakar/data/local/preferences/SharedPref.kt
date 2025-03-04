package com.senior25.tzakar.data.local.preferences

import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.PrefsDataStoreManager.getSharedPreference
import com.senior25.tzakar.helper.PreferenceHelper.clearPreferencesDataStore
import com.senior25.tzakar.helper.PreferenceHelper.getFromDataStore
import com.senior25.tzakar.helper.PreferenceHelper.saveToDataStore
import com.senior25.tzakar.ktx.ifEmpty
import com.senior25.tzakar.platform_specific.getApplicationConfig
import io.ktor.util.decodeBase64String
import okio.ByteString.Companion.decodeBase64

object SharedPref {

    fun clearPref() { getSharedPreference().clearPreferencesDataStore() }

    private fun appFlowStep()  = "${getApplicationConfig().id}.app_flow_step"

    var appState: AppState
        get() = AppState.valueOf(appState())
        set(appState)  = getSharedPreference().saveToDataStore(appFlowStep(),appState.name)

    private fun appState() = getSharedPreference().getFromDataStore(appFlowStep())?:AppState.NONE.name

    private fun registrationAppFlow() = "${getApplicationConfig().id}.registration_flow_step"

    var registrationState: RegistrationState
        get() = RegistrationState.valueOf(registrationState())
        set(appState)  = getSharedPreference().saveToDataStore(registrationAppFlow(),appState.name)

    private fun registrationState() = getSharedPreference().getFromDataStore(registrationAppFlow())
        ?: RegistrationState.SIGN_UP.name

    private fun keyLanguageSelected()  = "${getApplicationConfig().id}.pref_language_selected"

    var selectedLanguage: String
        set(id) = getSharedPreference().saveToDataStore(keyLanguageSelected(),id)
        get() = selectedLanguage()

    private fun selectedLanguage() = getSharedPreference().getFromDataStore(keyLanguageSelected())?:"en"

    private fun keyIsRememberMe()  = "${getApplicationConfig().id}.pref_language_selected"
    var isRememberMeChecked: Boolean
        set(id) = getSharedPreference().saveToDataStore(keyIsRememberMe(),id)
        get() = isRememberMe()
    private fun isRememberMe() = getSharedPreference().getFromDataStore(keyIsRememberMe())?:false

    private fun notificationStatus()  = "${getApplicationConfig().id}.pref_is_notification_permission_status"

    var notificationPermissionStatus: NotificationStatus
        get() = NotificationStatus.getByValue(notificationPermissionStatus())
        set(status)  = getSharedPreference().saveToDataStore(notificationStatus(),status.value)

    private fun notificationPermissionStatus() =
        getSharedPreference().getFromDataStore(notificationStatus())?: NotificationStatus.UNKNOWN.value

    var loggedInEmail: String?
       private set(email) {}
        get() = loggedInProfile?.email?.ifEmpty { null }

    private fun keyLoggedInProfile()  = "${getApplicationConfig().id}.pref_logged_in_profile"
    var loggedInProfile: UserProfile?
        set(profile) = getSharedPreference().saveToDataStore(keyLoggedInProfile(),profile)
        get() = getLoggedInProfileValue()
    private fun getLoggedInProfileValue() = getSharedPreference().getFromDataStore(keyLoggedInProfile())?:UserProfile()
}

enum class AppState { REGISTRATION, MAIN_ACTIVITY, NONE }

enum class RegistrationState { SIGN_IN,SIGN_UP }

enum class NotificationStatus(val value: Int) {
    UNKNOWN(3),OFF(2), ON(1);
    companion object {
        private val VALUES = entries.toTypedArray()
        fun getByValue(value: Int) = VALUES.firstOrNull { it.value == value } ?: UNKNOWN
    }
}




