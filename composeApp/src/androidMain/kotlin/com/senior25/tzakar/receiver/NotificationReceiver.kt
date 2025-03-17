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
import  com.senior25.tzakar.helper.notification.*
import com.senior25.tzakar.ktx.decodeJson

class NotificationReceiver : BroadcastReceiver() {
    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val notificationModel = intent.getStringExtra("notificationModel")?.decodeJson(NotificationModel())

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