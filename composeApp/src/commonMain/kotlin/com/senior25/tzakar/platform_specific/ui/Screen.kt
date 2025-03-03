package com.senior25.tzakar.platform_specific.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp


@Composable
expect fun getScreenWidth(): Dp

@Composable
expect fun getScreenHeight(): Dp