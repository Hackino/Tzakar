package com.senior25.tzakar.receiver

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.preferences.NotificationStatus
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.notification.NotificationHelper
import com.senior25.tzakar.ktx.decodeJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotificationReceiver : BroadcastReceiver(), KoinComponent {

    private val mainRepository: MainRepository by inject()

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val notificationModel = intent.getStringExtra("notificationModel")?.decodeJson(NotificationModel())

        CoroutineScope(Dispatchers.Default).launch {
            if (notificationModel != null) {
                mainRepository.insertNotification(notificationModel)
//                mainRepository.updateReminder(notificationModel)
            }
        }

        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(notificationModel?.title)
            .setContentText(notificationModel?.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        if(SharedPref.notificationPermissionStatus == NotificationStatus.ON)
            NotificationManagerCompat.from(context).notify(notificationModel?.title.hashCode(), notification)
    }
}