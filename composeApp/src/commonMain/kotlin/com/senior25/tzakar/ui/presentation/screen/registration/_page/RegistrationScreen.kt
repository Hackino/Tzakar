package com.senior25.tzakar.ui.presentation.screen.registration._page

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.ktx.koinNavigatorScreenModel
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreen

data class RegistrationLauncher(val test:String? = null): Screen {
    @Composable
    override fun Content() {
//        Navigator(screen =  RegistrationScreen())
        val navigator = LocalNavigator.currentOrThrow
        navigator.push(RegistrationScreen())
    }
}

class RegistrationScreen: Screen {
    @Composable
    override fun Content() {
//        val navigator = LocalNavigator.currentOrThrow
//        val screenModel = navigator.koinNavigatorScreenModel<RegistrationScreenViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        navigator.push(SignInScreen())
//        Navigator(
//            screen =  SignInScreen(),
//        ) { navigator ->
//            SlideTransition(navigator)
//        }


    }
}