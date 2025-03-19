package com.senior25.tzakar.helper.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.senior25.tzakar.data.local.database.dao.NotificationDao
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.preferences.NotificationStatus
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.data.repositories.MainRepositoryImpl
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.encodeToJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import platform.Foundation.NSUUID
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

actual object NotificationHelper: KoinComponent {
    private val mainRepository: MainRepository by inject()

    actual fun showNotification(notificationModel:NotificationModel) {

        val canterExternal = UNUserNotificationCenter.currentNotificationCenter()

        canterExternal.getPendingNotificationRequestsWithCompletionHandler { requests ->
            val notificationRequests = requests?.filterIsInstance<UNNotificationRequest>()

            val existingRequest = notificationRequests?.firstOrNull { it.identifier == notificationModel.referenceId }

            if (existingRequest == null) {
                val content = UNMutableNotificationContent().apply {
                    this.setTitle(notificationModel.title ?: "")
                    this.setBody(notificationModel.body ?: "")
                    this.setSound(UNNotificationSound.defaultSound())
                    this.setUserInfo(mapOf("notificationData" to notificationModel.encodeToJson()))
                }

                val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(60.0, false)

                val request = UNNotificationRequest.requestWithIdentifier(
                    notificationModel.referenceId ?: "",
                    content,
                    trigger
                )

                val centerInternal = UNUserNotificationCenter.currentNotificationCenter()

                centerInternal.delegate = object : NSObject(), UNUserNotificationCenterDelegateProtocol {

                    override fun userNotificationCenter(
                        center: UNUserNotificationCenter,
                        willPresentNotification: UNNotification,
                        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
                    ) {

                        val notificationData = willPresentNotification.request.content.userInfo

                        val model = notificationData["notificationData"]?.toString()
                            ?.decodeJson(NotificationModel())

                        CoroutineScope(Dispatchers.Default).launch {
                            if (model != null) mainRepository.insertNotification(model)
                        }
                        withCompletionHandler(UNNotificationPresentationOptionAlert or UNNotificationPresentationOptionSound)
                    }

                    override fun userNotificationCenter(
                        center: UNUserNotificationCenter,
                        didReceiveNotificationResponse: UNNotificationResponse,
                        withCompletionHandler: () -> Unit
                    ) {
                        withCompletionHandler()
                    }
                }

                if (SharedPref.notificationPermissionStatus == NotificationStatus.ON) {
                    centerInternal.addNotificationRequest(request) { error ->
                        if (error != null) println("Error -> $error") else println("Notification sent")
                    }
                }
            }
        }
    }

    actual fun isNotificationPermissionGranted(result:(Boolean)->Unit) {
        UNUserNotificationCenter.currentNotificationCenter().getNotificationSettingsWithCompletionHandler { settings ->
            result(settings?.authorizationStatus == UNAuthorizationStatusAuthorized)
        }
    }

    @Composable
    actual fun requestNotificationPermission(onResult: (Boolean) -> Unit) {
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            scope.launch(Dispatchers.Main) {
                UNUserNotificationCenter.currentNotificationCenter()
                    .requestAuthorizationWithOptions(
                        options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
                    ) { granted, error -> scope.launch (Dispatchers.IO) { onResult(granted) } }
            }
        }
    }

    actual fun cancelNotification(ids: List<String>) {
        UNUserNotificationCenter
            .currentNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(ids)
    }
}