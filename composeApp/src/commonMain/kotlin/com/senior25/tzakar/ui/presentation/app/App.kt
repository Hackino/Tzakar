package com.senior25.tzakar.ui.presentation.app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.senior25.tzakar.data.local.preferences.AppState
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.ui.presentation.graph.NavigationModel
import com.senior25.tzakar.ui.presentation.screen.common.CommonViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

// firebase

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
        AppState.NONE -> AppGraph(AppGraph.Registration,routingNavController)
        else -> AppGraph(AppGraph.Registration,routingNavController)
    }
}

@Preview
@Composable
fun ScreenIntentListener(baseViewModel:CommonViewModel,routingNavController: NavHostController) {
    val navState by baseViewModel.navigateTo.collectAsState(initial = NavigationModel())
    navState.navId.let {
        if (it != 0) {
            routingNavController.navigate(route = it);
            baseViewModel.navigateTo()
        }
    }
}

interface RoutingScreenInteraction{
    fun getAppState():AppState
}