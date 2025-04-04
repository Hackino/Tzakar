package com.senior25.tzakar.platform_specific.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
actual  fun MapView(modifier: Modifier,
                    cameraLongLat:List<Double>,
                    markerLongLat:List<Double>?,
                    setMarker:Boolean,
                    showControls:Boolean ,
                    onMarkerSet:(Double,Double)->Unit
){
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        val coordinates = LatLng(cameraLongLat[1], cameraLongLat[0])

        val markerPosition = remember { mutableStateOf(LatLng(0.0, 0.0)) }

        val markerState = rememberMarkerState(
            position = markerPosition.value
        )
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(coordinates, 13f)
        }
        LaunchedEffect(markerLongLat) {
            markerLongLat?.let {
                markerPosition.value = LatLng(it.getOrNull(1) ?: 0.0, it.getOrNull(0) ?: 0.0)
                markerState.position =  markerPosition.value
            }
        }

        LaunchedEffect(cameraLongLat) {
            cameraLongLat.let {
                cameraPositionState.position =
                    CameraPosition.fromLatLngZoom(
                        LatLng(it.getOrNull(1) ?: 0.0,
                            it.getOrNull(0) ?: 0.0),
                        cameraPositionState.position.zoom
                    )
            }
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(
                zoomGesturesEnabled = showControls,
                zoomControlsEnabled = showControls,
                tiltGesturesEnabled = showControls,
                scrollGesturesEnabled = showControls,
                rotationGesturesEnabled = false
            ),
            onMapClick = if (setMarker){{ latLng ->
                markerPosition.value = latLng
                onMarkerSet(latLng.longitude, latLng.latitude)
            }}else null
        ) {
            if (markerState.position.latitude != 0.0 || markerState.position.longitude != 0.0) {
                println("Drawing marker at: ${markerState.position}")
                Marker(state = markerState)
            }
        }
    }
}

