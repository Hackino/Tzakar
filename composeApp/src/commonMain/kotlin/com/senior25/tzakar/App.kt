package com.senior25.tzakar

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.senior25.tzakar.data.local.preferences.AppState
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreen
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

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
    Scaffold (backgroundColor = MyColors.colorWhite) {
        when (interaction?.getAppState()) {
            AppState.NONE -> SignInScreen()
            else -> SignInScreen()
        }
    }
}

interface RoutingScreenInteraction{
    fun getAppState():AppState
}