package com.senior25.tzakar.platform_specific

import platform.CoreLocation.CLCircularRegion
import platform.CoreLocation.CLLocationCoordinate2D
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLRegion

actual class GeofenceManager {

    /**
     * Start monitoring a geofence at the given location with a specific radius.
     */

    private val locationManager: CLLocationManager = CLLocationManager().apply {
        // Configure location manager if needed.
    }
    actual fun startGeofence(
        latitude: Double,
        longitude: Double,
        radius: Float
    ) {

//        val center = CLLocationCoordinate2D(
//            latitude = latitude,
//            longitude = longitude
//        )
//        val region = CLCircularRegion(center = center, radius = radius.toDouble(), identifier = "myGeofence").apply {
//            notifyOnEntry = true
//            notifyOnExit = true
//        }

//        locationManager.startMonitoringForRegion(region)
    }

    /**
     * Stop monitoring the current geofence.
     */
    actual fun stopGeofence() {
        val region: CLRegion? = locationManager.monitoredRegions.firstOrNull() as? CLRegion
        region?.let { locationManager.stopMonitoringForRegion(it) }
    }

}