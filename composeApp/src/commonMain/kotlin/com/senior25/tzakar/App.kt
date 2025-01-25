package com.senior25.tzakar

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.senior25.tzakar.data.local.preferences.AppState
import com.senior25.tzakar.ui.presentation.graph.RegistrationGraph
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

//handle navigation from 1 place
// firebase

@Preview
@Composable
fun App() {
    KoinContext {
        MaterialTheme { RoutingScreen() }
    }
}

@Preview
@Composable
fun RoutingScreen(interaction:RoutingScreenInteraction? = null) {
    val navController = rememberNavController()
    when (interaction?.getAppState()) {
        AppState.NONE -> RegistrationGraph(navController)
        else -> RegistrationGraph(navController)
    }
}

interface RoutingScreenInteraction{
    fun getAppState():AppState
}