package com.senior25.tzakar.platform_specific.map

interface MapInteraction {
    fun getMarkerLong():Double?
    fun getMarkerLat():Double?
    fun onMarkerSet(long:Double,lat:Double)
    fun getCameraLong():Double
    fun getCameraLat():Double
}