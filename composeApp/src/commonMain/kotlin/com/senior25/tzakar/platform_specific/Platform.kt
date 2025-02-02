package com.senior25.tzakar.platform_specific

import androidx.compose.runtime.Composable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


expect fun exitApp()