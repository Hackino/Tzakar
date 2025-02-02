package com.senior25.tzakar.ui.presentation.app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.data.local.preferences.AppState
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenLauncher
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationLauncher
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Preview
@Composable
fun App() {
    KoinContext {
        MaterialTheme {
            RoutingScreen(interaction = object : RoutingScreenInteraction {
                override fun getAppState(): AppState  = SharedPref.appState
            })
        }
    }
}

@Composable
fun RoutingScreen(interaction: RoutingScreenInteraction? = null) {
    val launcher = when (interaction?.getAppState()) {
        AppState.NONE -> RegistrationLauncher()
        AppState.MAIN_ACTIVITY->MainScreenLauncher()
        else -> RegistrationLauncher()
    }
    Navigator(launcher){ navigator->
        SlideTransition(navigator)
    }
}

interface RoutingScreenInteraction{
    fun getAppState():AppState
}