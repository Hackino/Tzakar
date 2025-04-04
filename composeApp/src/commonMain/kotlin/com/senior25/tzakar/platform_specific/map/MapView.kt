package com.senior25.tzakar.platform_specific.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

@Composable
expect fun MapView(modifier: Modifier ,cameraLongLat:List<Double>,markerLongLat:List<Double>?,
                   setMarker:Boolean = true,showControls:Boolean ,onMarkerSet:(Double,Double)->Unit)

@Composable
fun StaticMapSnapshot(
    lat: Double,
    lng: Double,
    modifier: Modifier = Modifier,
    width: Int = 400,
    height: Int = 150,
    zoom: Int = 13
) {

//    val mapUrl = remember(lat, lng) {
//        "https://maps.locationiq.com/v3/staticmap?" +
//                "key=YOUR_LOCATIONIQ_TOKEN" +
//                "&center=$lat,$lng" +
//                "&zoom=$zoom&size=${width}x$height" +
//                "&markers=icon:small-red-cutout|$lat,$lng"
//    }

    val mapUrl = remember(lat, lng) {
        "https://api.mapbox.com/styles/v1/mapbox/streets-v11/static/" +
                "pin-s+ff0000($lng,$lat)/$lng,$lat,$zoom/${width}x$height" +
                "?access_token=pk.eyJ1IjoiaGFja2lubyIsImEiOiJjbTkzOWN1aWswaWExMmlxdnRqMzZuYXB6In0.OZV2b57UG5N3C3hhdYzxEw"
    }

println(mapUrl)
    AsyncImage(
        model = mapUrl,
        contentDescription = "Map Snapshot",
        modifier = modifier,
        contentScale = ContentScale.Crop
    )
}