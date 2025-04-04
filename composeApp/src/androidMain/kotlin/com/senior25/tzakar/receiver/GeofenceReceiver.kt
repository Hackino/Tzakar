package com.senior25.tzakar.receiver

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.net.Uri
import androidx.annotation.CallSuper
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import com.senior25.tzakar.R
import com.senior25.tzakar.data.local.preferences.NotificationStatus
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.domain.MainRepository
import com.senior25.tzakar.helper.ApplicationProvider
import com.senior25.tzakar.helper.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GeofenceBroadcastReceiver : BroadcastReceiver(), KoinComponent {

    private val mainRepository: MainRepository by inject()

    @CallSuper
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null)return
        GeofencingEvent.fromIntent(intent)?.apply {
            handleGeofenceError(context)
            context?.let { handleGeofenceTransition(it) }
        }
    }

    private fun GeofencingEvent.handleGeofenceError(context: Context?,){
        if (hasError()) { GeofenceStatusCodes.getStatusCodeString(errorCode);return }
    }

    @SuppressLint("MissingPermission")
    private fun GeofencingEvent.handleGeofenceTransition(context: Context){
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            val triggeringGeoFences = triggeringGeofences
            CoroutineScope(Dispatchers.IO).launch{
                if (triggeringGeoFences.isNullOrEmpty())return@launch
                try {
                    mainRepository.getReminderById(triggeringGeoFences[0].requestId).collectLatest {
                        val soundUri = it?.sound?.let {
                            Uri.parse(
                                "android.resource://${ApplicationProvider.application.packageName}/raw/${
                                    it.replace(
                                        ".wav",
                                        ""
                                    )
                                }"
                            )
                        } ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                        NotificationHelper.createNotificationChannel(
                            it?.id,
                            it?.sound
                        )
                        val notification = NotificationCompat.Builder(
                            context,
                            it?.id ?: "Default-Channel"
                        ).setSmallIcon(R.mipmap.ic_launcher_round).apply {
                            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
                                setSound(soundUri, AudioManager.STREAM_NOTIFICATION)
                            }
                        }.setContentTitle(it?.title).setContentText(it?.description)
                            .setPriority(NotificationManager.IMPORTANCE_HIGH).setAutoCancel(false)
                            .build()
                        if (SharedPref.notificationPermissionStatus == NotificationStatus.ON)
                            NotificationManagerCompat.from(context).notify(it?.title.hashCode(), notification)
                    }
                }catch (e:Exception){

                }
            }
        }
    }
}
