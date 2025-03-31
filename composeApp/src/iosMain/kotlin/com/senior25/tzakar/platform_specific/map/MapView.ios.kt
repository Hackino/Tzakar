package com.senior25.tzakar.platform_specific.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.interop.UIKitViewController
import com.senior25.tzakar.LocalNativeViewFactory
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun MapView(modifier: Modifier ,cameraLongLat:List<Double>,markerLongLat:List<Double>?,  showControls:Boolean ,onMarkerSet:(Double,Double)->Unit) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        factory = {
            val mapViewController = factory.createGoogleMap(object :MapInteraction{
                override fun getMarkerLong() = markerLongLat?.getOrNull(0)
                override fun getMarkerLat()  = markerLongLat?.getOrNull(1)
                override fun getCameraLong() = cameraLongLat[0]
                override fun getCameraLat()  = cameraLongLat[1]
                override fun onMarkerSet(long:Double,lat:Double) {}
                override fun showControl(): Boolean  = showControls
            })
            mapViewController
        },
        modifier = Modifier.fillMaxSize(),
    )
}