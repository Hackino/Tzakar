package com.senior25.tzakar.helper.notification


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.senior25.tzakar.data.local.model.notification.NotificationModel
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.ApplicationProvider
import com.senior25.tzakar.ktx.encodeToJson
import com.senior25.tzakar.receiver.NotificationReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual object NotificationHelper: KoinComponent {
    private val mainRepository: MainRepository by inject()

    fun createNotificationChannel(id:String?,sound:String?) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val soundUri =sound?.let {
                Uri.parse("android.resource://${ApplicationProvider.application.packageName}/raw/${it.replace(".wav","")}")
            }?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val name = "Default Channel"
            val descriptionText = "This is a test notification channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id?:"Default-Channel", name, importance).apply {
                description = descriptionText
                setSound(soundUri, attributes) // For Android 8.0+

            }
            val notificationManager: NotificationManager =
                ApplicationProvider.application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    actual fun showNotification(notificationModel: NotificationModel) {
        val context = ApplicationProvider.application
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("notificationModel", notificationModel.encodeToJson())
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationModel.referenceId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = (notificationModel.dateTimeEpoch?.toLong()?:0L)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

    actual fun isNotificationPermissionGranted(result:(Boolean)->Unit) {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            result(ContextCompat.checkSelfPermission(
                ApplicationProvider.application,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED)
        } else {
            result(true)
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
                    isNotificationPermissionGranted{
                        onResult(it)
                    }
                } else {
                    scope.launch (Dispatchers.IO) { onResult(true) }
                }
            }
        }
    }

    actual fun cancelNotification(ids: List<String>) {
        val context = ApplicationProvider.application
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        ids.forEach {currentId->
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                currentId.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}