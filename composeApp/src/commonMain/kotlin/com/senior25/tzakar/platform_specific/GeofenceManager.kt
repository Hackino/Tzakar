package com.senior25.tzakar.platform_specific



expect class GeofenceManager {
    /**
     * Start monitoring a geofence at the given location with a specific radius.
     */
    fun startGeofence(latitude: Double, longitude: Double, radius: Float)

    /**
     * Stop monitoring the current geofence.
     */
    fun stopGeofence()
}