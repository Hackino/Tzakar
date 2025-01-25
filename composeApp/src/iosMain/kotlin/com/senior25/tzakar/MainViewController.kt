package com.senior25.tzakar

import androidx.compose.ui.window.ComposeUIViewController
import com.senior25.tzakar.di.initializeKoin

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin() }
) { App() }