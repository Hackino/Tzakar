package com.senior25.tzakar.ui.presentation.screen.main._page

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@OptIn(KoinExperimentalAPI::class)
@Preview
@Composable
fun MainScreen(
    routingNavController: NavHostController,
) {
    val viewModel = koinViewModel<RegistrationScreenViewModel>()
}