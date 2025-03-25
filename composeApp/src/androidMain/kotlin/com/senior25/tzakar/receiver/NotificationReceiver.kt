package com.senior25.tzakar.receiver

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.senior25.tzakar.R
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.data.local.preferences.NotificationStatus
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.ApplicationProvider
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
        val soundUri =notificationModel?.sound?.let {
            Uri.parse("android.resource://${ApplicationProvider.application.packageName}/raw/${it.replace(".wav", "")}")
        }?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

//        val mediaPlayer = MediaPlayer().apply {
//            try {
//                setDataSource(context, soundUri)
//                setAudioAttributes(
//                    AudioAttributes.Builder()
//                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                        .build()
//                )
//                prepare()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//
//        mediaPlayer.start() // Start playing the sound

        NotificationHelper.createNotificationChannel(notificationModel?.id,notificationModel?.sound)
        val notification = NotificationCompat.Builder(context, notificationModel?.id?:"Default-Channel")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .apply {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                    setSound(soundUri, AudioManager.STREAM_NOTIFICATION)
                }
            }
            .setContentTitle(notificationModel?.title)
            .setContentText(notificationModel?.body)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(false)
            .build()
        if(SharedPref.notificationPermissionStatus == NotificationStatus.ON)
            NotificationManagerCompat.from(context).notify(notificationModel?.title.hashCode(), notification)
    }
}