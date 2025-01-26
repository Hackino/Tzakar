package com.senior25.tzakar.ui.presentation.app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.senior25.tzakar.data.local.preferences.AppState
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.ui.graph.AppGraph
import com.senior25.tzakar.ui.graph.screens.RoutingScreens
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

@Preview
@Composable
fun RoutingScreen(interaction: RoutingScreenInteraction? = null) {
    val routingNavController = rememberNavController()
    when (interaction?.getAppState()) {
        AppState.NONE -> AppGraph(RoutingScreens.Registration,routingNavController)
        else -> AppGraph(RoutingScreens.Registration,routingNavController)
    }
}

interface RoutingScreenInteraction{
    fun getAppState():AppState
}