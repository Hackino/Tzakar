package com.senior25.tzakar.ui.presentation.screen.registration._page

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Preview
@Composable
fun RegistrationScreen(
    routingNavController: NavHostController,
) {
    val viewModel = koinViewModel<RegistrationScreenViewModel>()
}
