package com.senior25.tzakar.platform_specific.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
actual  fun MapView(modifier: Modifier ,cameraLongLat:List<Double>,markerLongLat:List<Double>?,onMarkerSet:(Double,Double)->Unit)  {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val coordinates = LatLng(cameraLongLat[0], cameraLongLat[1])

        val markerState = rememberMarkerState(
            position = LatLng(
                markerLongLat?.getOrNull(0)?:0.0,
                markerLongLat?.getOrNull(1)?:0.0
            )
        )
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(coordinates, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            listOf(markerState.position.latitude,markerState.position.longitude)
                .filter { it != 0.0 }.ifEmpty { null }.let { Marker(state = markerState,) }
        }
    }
}