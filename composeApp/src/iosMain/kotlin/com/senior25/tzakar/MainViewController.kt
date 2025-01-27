package com.senior25.tzakar

import androidx.compose.ui.window.ComposeUIViewController
import com.senior25.tzakar.di.initializeKoin
import com.senior25.tzakar.ui.presentation.app.App
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseApp
import dev.gitlive.firebase.auth.FirebaseAuth

fun MainViewController() = ComposeUIViewController(
    configure = { initializeKoin()
    }
) { App() }