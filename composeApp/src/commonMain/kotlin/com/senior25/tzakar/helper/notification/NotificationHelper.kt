package com.senior25.tzakar.helper.notification

import androidx.compose.runtime.Composable
import com.senior25.tzakar.data.local.model.notification.NotificationModel

expect object NotificationHelper {

     fun isNotificationPermissionGranted(result:(Boolean)->Unit)
     @Composable
    fun requestNotificationPermission(onResult: (Boolean) -> Unit)
    fun showNotification(notificationModel: NotificationModel)
    fun cancelNotification(ids: List<String>)
}