package com.senior25.tzakar.ui.presentation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.registration.forget_password.ForgotPasswordScreen
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreen
import com.senior25.tzakar.ui.presentation.screen.registration.sign_up.SignUpScreen
import kotlinx.serialization.Serializable

@Serializable
sealed interface RegistrationScreens{
    @Serializable
    data object SignIn: RegistrationScreens

    @Serializable
    data object SignUp : RegistrationScreens

    @Serializable
    data object Forgot: RegistrationScreens
}

@Composable
fun RegistrationGraph(viewModel:RegistrationScreenViewModel,navController: NavHostController) {
    NavHost(navController = navController, startDestination = RegistrationScreens.SignIn) {
        composable<RegistrationScreens.SignIn> { SignInScreen(viewModel,navController) }
        composable<RegistrationScreens.SignUp> { SignUpScreen(viewModel,navController) }
        composable<RegistrationScreens.Forgot> { ForgotPasswordScreen(viewModel,navController) }
    }
}