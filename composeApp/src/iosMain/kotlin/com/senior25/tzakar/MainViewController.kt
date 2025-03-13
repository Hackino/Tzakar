
package com.senior25.tzakar

import androidx.compose.ui.window.ComposeUIViewController
import com.senior25.tzakar.di.initializeKoin
import com.senior25.tzakar.ui.presentation.app.App

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin(config = {}) }
) { App() }