package com.senior25.tzakar.platform_specific.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.senior25.tzakar.LocalNativeViewFactory


@Composable
actual fun MapView(modifier: Modifier ,cameraLongLat:List<Double>,markerLongLat:List<Double>?,  showControls:Boolean ,onMarkerSet:(Double,Double)->Unit) {
    val factory = LocalNativeViewFactory.current

    val interaction = object :MapInteraction{
        override fun getMarkerLong() = markerLongLat?.getOrNull(0)
        override fun getMarkerLat()  = markerLongLat?.getOrNull(1)
        override fun getCameraLong() = cameraLongLat[0]
        override fun getCameraLat()  = cameraLongLat[1]
        override fun onMarkerSet(long:Double,lat:Double) { onMarkerSet.invoke(long,lat) }
        override fun showControl(): Boolean  = showControls
    }
    UIKitViewController(
        factory = { factory.createGoogleMap(interaction) },
        update = { viewController ->
            factory.updateGoogleMapMarker(
                viewController,
                latitude = markerLongLat?.getOrNull(1) ?: 0.0,
                longitude = markerLongLat?.getOrNull(0) ?: 0.0
            )
        },
        modifier = Modifier.fillMaxSize(),
    )
}