package com.senior25.tzakar.platform_specific

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.senior25.tzakar.helper.ApplicationProvider
import com.senior25.tzakar.receiver.GeofenceBroadcastReceiver

actual class GeofenceManager {


    @SuppressLint("MissingPermission")
    actual fun startGeofence(
        latitude: Double,
        longitude: Double,
        radius: Float
    ) {
        val geofence = Geofence.Builder()
            .setRequestId("myGeofence")
            .setCircularRegion(latitude, longitude, radius)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()

        val geofencingRequest = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        val intent = Intent(ApplicationProvider.application, GeofenceBroadcastReceiver::class.java)
        val geofencePendingIntent = PendingIntent.getBroadcast(
            ApplicationProvider.application,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        geofencePendingIntent.let{
            LocationServices.getGeofencingClient(ApplicationProvider.application).addGeofences(geofencingRequest, it)
                ?.addOnSuccessListener {}?.addOnFailureListener { e -> }
        }

    }


    actual fun stopGeofence() {
        val intent = Intent(ApplicationProvider.application, GeofenceBroadcastReceiver::class.java)
        val geofencePendingIntent = PendingIntent.getBroadcast(
            ApplicationProvider.application,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        LocationServices
            .getGeofencingClient(ApplicationProvider.application)
            .removeGeofences(geofencePendingIntent)

    }

}