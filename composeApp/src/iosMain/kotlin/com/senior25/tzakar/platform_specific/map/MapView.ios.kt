package com.senior25.tzakar.platform_specific.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.interop.UIKitViewController
import com.senior25.tzakar.LocalNativeViewFactory
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual  fun MapView(modifier: Modifier ,longLat:List<Double>) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        factory = {
            val mapViewController = factory.createGoogleMap()
            mapViewController
        },
        modifier = Modifier.fillMaxSize(),
    )
}