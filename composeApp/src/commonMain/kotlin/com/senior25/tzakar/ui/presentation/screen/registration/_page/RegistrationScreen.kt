package com.senior25.tzakar.ui.presentation.screen.registration._page

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreen

data class RegistrationLauncher(val test:String? = null): Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        navigator.push(RegistrationScreen())
    }
}

class RegistrationScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        navigator.push(SignInScreen())
    }
}