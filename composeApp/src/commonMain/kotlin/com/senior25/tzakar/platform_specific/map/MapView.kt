package com.senior25.tzakar.platform_specific.map

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun MapView(modifier: Modifier,longLat:List<Double>)