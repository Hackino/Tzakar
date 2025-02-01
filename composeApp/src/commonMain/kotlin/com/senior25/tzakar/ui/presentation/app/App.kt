package com.senior25.tzakar.ui.presentation.app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.data.local.preferences.AppState
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Preview
@Composable
fun App() {
    KoinContext {
        MaterialTheme {
           val routingPage  =  RoutingScreen(interaction = object : RoutingScreenInteraction {
                override fun getAppState(): AppState  = SharedPref.appState
            })

            Navigator(SignInScreen()){navigator->
                SlideTransition(navigator)
            }
        }
    }
}

data class RoutingScreen(val interaction: RoutingScreenInteraction? = null):Screen {

    @Composable
    override fun Content() {
//        val routingNavController = rememberNavController()
//        when (interaction?.getAppState()) {
//            AppState.NONE -> //AppGraph(RoutingScreens.Registration,routingNavController)
//            else -> //AppGraph(RoutingScreens.Registration,routingNavController)
//        }
    }
}

interface RoutingScreenInteraction{
    fun getAppState():AppState
}