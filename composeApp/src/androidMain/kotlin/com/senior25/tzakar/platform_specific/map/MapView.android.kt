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
actual  fun MapView(modifier: Modifier ,longLat:List<Double>)  {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val coordinates = LatLng(longLat[0], longLat[1])
        val markerState = rememberMarkerState(position = coordinates)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(coordinates, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
                state = markerState,
            )
        }
    }
}