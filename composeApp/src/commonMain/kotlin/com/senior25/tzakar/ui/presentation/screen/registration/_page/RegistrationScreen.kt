package com.senior25.tzakar.ui.presentation.screen.registration._page

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.senior25.tzakar.ui.presentation.app.ScreenIntentListener
import com.senior25.tzakar.ui.presentation.graph.RegistrationGraph
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Preview
@Composable
fun RegistrationScreen(routingNavController: NavHostController) {
    val viewModel = koinViewModel<RegistrationScreenViewModel>()
    val navController = rememberNavController()
    ScreenIntentListener(viewModel,routingNavController)
    Scaffold (backgroundColor = MyColors.colorWhite) {
        RegistrationGraph(viewModel,navController)
    }
}
