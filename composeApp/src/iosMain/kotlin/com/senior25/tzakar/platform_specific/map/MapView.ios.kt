package com.senior25.tzakar.platform_specific.map

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitViewController
import com.senior25.tzakar.LocalNativeViewFactory
import platform.UIKit.UIViewController


@Composable
actual fun MapView(modifier: Modifier ,
                   cameraLongLat:List<Double>,
                   markerLongLat:List<Double>?,
                   setMarker:Boolean,
                   showControls:Boolean ,
                   onMarkerSet:(Double,Double)->Unit
) {
    val factory = LocalNativeViewFactory.current

    val interaction = remember(cameraLongLat, markerLongLat, showControls) {
        object : MapInteraction {
            override fun getMarkerLong() = markerLongLat?.getOrNull(0)
            override fun getMarkerLat() = markerLongLat?.getOrNull(1)
            override fun getCameraLong() = cameraLongLat[0]
            override fun getCameraLat() = cameraLongLat[1]
            override fun enableSetMarker() = setMarker
            override fun onMarkerSet(long: Double, lat: Double) { onMarkerSet(long, lat) }
            override fun showControl(): Boolean = showControls
        }
    }

    val currentMarkerLat = markerLongLat?.getOrNull(1) ?: 0.0
    val currentMarkerLong = markerLongLat?.getOrNull(0) ?: 0.0

    val updateLambda: (UIViewController) -> Unit = remember(currentMarkerLat, currentMarkerLong) {
        { viewController ->
            factory.updateGoogleMapMarker(
                viewController,
                latitude = currentMarkerLat,
                longitude = currentMarkerLong
            )
        }
    }

    UIKitViewController(
        factory = { factory.createGoogleMap(interaction) },
        update = updateLambda,
        modifier = Modifier.fillMaxSize(),
    )
}