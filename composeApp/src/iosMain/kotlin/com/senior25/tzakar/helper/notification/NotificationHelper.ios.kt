package com.senior25.tzakar.helper.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.senior25.tzakar.data.local.database.dao.ReminderDao
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.preferences.NotificationStatus
import com.senior25.tzakar.data.local.preferences.SharedPref
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import platform.Foundation.NSUUID
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

actual object NotificationHelper {
//    private val reminderDao: ReminderDao by inject(ReminderDao::class.java) // Inject DAO from Koin

    actual fun showNotification(notificationModel:NotificationModel) {
        val content = UNMutableNotificationContent().apply {
            this.setTitle(notificationModel.title?:"")
            this.setBody(notificationModel.body?:"")
            this.setSound(UNNotificationSound.defaultSound())
        }

        // convert date to timestamp to get trigger time
        val uuid = NSUUID.UUID().UUIDString()
        val trigger = UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
            60.0, false
        )
        val request = UNNotificationRequest.requestWithIdentifier(uuid, content,trigger)

        val center = UNUserNotificationCenter.currentNotificationCenter()

        center.delegate = object : NSObject(), UNUserNotificationCenterDelegateProtocol {
            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                willPresentNotification: UNNotification,
                withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
            ) {
                withCompletionHandler(UNNotificationPresentationOptionAlert)
            }

            override fun userNotificationCenter(
                center: UNUserNotificationCenter,
                didReceiveNotificationResponse: UNNotificationResponse,
                withCompletionHandler: () -> Unit
            ) {
                withCompletionHandler()
            }
        }

        if(SharedPref.notificationPermissionStatus == NotificationStatus.ON) {
            center.addNotificationRequest(request) { error ->
                if (error != null) {
                    println("Error -> $error")
                } else {
                    println("Notification sent")
                }
            }
        }
    }


    actual fun isNotificationPermissionGranted(): Boolean {
        var isGranted = false
        UNUserNotificationCenter.currentNotificationCenter().getNotificationSettingsWithCompletionHandler { settings ->
            isGranted = settings?.authorizationStatus == UNAuthorizationStatusAuthorized
        }
        return isGranted
    }

    @Composable
    actual fun requestNotificationPermission(onResult: (Boolean) -> Unit) {
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            scope.launch(Dispatchers.Main) {
                UNUserNotificationCenter.currentNotificationCenter()
                    .requestAuthorizationWithOptions(
                        options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
                    ) { granted, error ->
                        scope.launch (Dispatchers.IO) { onResult(granted) }
                    }
            }
        }
    }

    actual fun cancelNotification(ids: List<String>) {
        UNUserNotificationCenter
            .currentNotificationCenter()
            .removePendingNotificationRequestsWithIdentifiers(ids)

        // turn off from database

    }
}