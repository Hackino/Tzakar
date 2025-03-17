package com.senior25.tzakar.helper.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.senior25.tzakar.application.MyApplication
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.helper.ApplicationProvider


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.senior25.tzakar.data.local.preferences.NotificationStatus
import com.senior25.tzakar.data.local.preferences.SharedPref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

actual object NotificationHelper {
    private val CHANNEL_ID = "default_channel"

    init {
        createNotificationChannel()
    }

    private  fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val name = "Default Channel"
            val descriptionText = "This is a test notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                ApplicationProvider.application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    actual fun showNotification(notificationModel: NotificationModel) {
        val notification = NotificationCompat.Builder(   ApplicationProvider.application, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(notificationModel.title)
            .setContentText(notificationModel.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        if(SharedPref.notificationPermissionStatus == NotificationStatus.ON)
        NotificationManagerCompat.from(ApplicationProvider.application).notify(1, notification)
    }

    actual fun isNotificationPermissionGranted(): Boolean {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                ApplicationProvider.application,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true  // Below Android 13, permission is granted by default
        }
    }

    @Composable
    actual fun requestNotificationPermission(onResult: (Boolean) -> Unit) {
        val context = LocalActivity.current
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            CoroutineScope(Dispatchers.Main).launch {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                        1001
                    )
                    onResult(isNotificationPermissionGranted())
                } else {
                    scope.launch (Dispatchers.IO) { onResult(true) }
                }
            }
        }
    }
}