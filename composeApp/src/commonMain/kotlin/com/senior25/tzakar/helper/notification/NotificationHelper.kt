package com.senior25.tzakar.helper.notification

import androidx.compose.runtime.Composable
import com.senior25.tzakar.data.local.model.notification.NotificationModel

expect object NotificationHelper {
    fun isNotificationPermissionGranted(): Boolean
    @Composable
    fun requestNotificationPermission(onResult: (Boolean) -> Unit)
    fun showNotification(notificationModel: NotificationModel)
}