package com.senior25.tzakar.platform_specific.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit


@Composable
expect fun getScreenWidth(): Dp

@Composable
expect fun getScreenHeight(): Dp



@Composable
fun spToDp(sp: TextUnit): Dp {
    val density = LocalDensity.current
    return with(density) {
        sp.toDp()
    }
}
