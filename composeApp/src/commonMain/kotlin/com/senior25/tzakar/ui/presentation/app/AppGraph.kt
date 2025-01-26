package com.senior25.tzakar.ui.presentation.app

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreen
import com.senior25.tzakar.ui.presentation.screen.web.WebViewScreen


@kotlinx.serialization.Serializable
sealed interface AppGraph{
    @kotlinx.serialization.Serializable
    data object Registration: AppGraph

    @kotlinx.serialization.Serializable
    data object Web : AppGraph

    @kotlinx.serialization.Serializable
    data object Main: AppGraph
}

@Composable
fun AppGraph(startDestination :AppGraph,navController: NavHostController) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable<AppGraph.Registration> { RegistrationScreen(navController) }
        composable<AppGraph.Web> { WebViewScreen(navController) }
//        composable<RegistrationScreens.Forgot> { ForgotPasswordScreen(viewModel,navController) }
    }
}
