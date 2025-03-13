package com.senior25.tzakar.helper.notification

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNAuthorizationStatusAuthorized
import platform.UserNotifications.UNUserNotificationCenter

actual object NotificationHelper {

    actual fun showNotification(notificationModel:NotificationModel) {
//        val content = UNMutableNotificationContent().apply {
////            this.title = notificationModel.title
////            this.body = notificationModel.body
////            this.sound = UNNotificationSound.defaultSound()
//        }
//
//        val trigger = UNTimeIntervalNotificationTrigger.timeIntervalNotificationTriggerWithTimeInterval(
//            timeInterval = 1.0,
//            repeats = false
//        )
//
//        val request = UNNotificationRequest.requestWithIdentifier(
//            identifier = "localNotification",
//            content = content,
//            trigger = trigger
//        )
//
//        UNUserNotificationCenter.currentNotificationCenter().addNotificationRequest(request) { error ->
//            error?.let { println("Error: ${it.localizedDescription}") }
//        }
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
}