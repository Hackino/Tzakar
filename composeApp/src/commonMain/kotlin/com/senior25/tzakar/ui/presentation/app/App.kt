package com.senior25.tzakar.ui.presentation.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.ScreenTransition
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.data.local.preferences.AppState
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.ui.presentation.app.AppNavigator.launcherScreens

import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenLauncher
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationLauncher
import com.senior25.tzakar.ui.presentation.screen.registration._page.RegistrationScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

object AppNavigator{

     val launcherScreens = listOf<String?>(
         RegistrationLauncher::class.simpleName,
         RegistrationScreen::class.simpleName,
         MainScreenLauncher::class.simpleName
    )
    private val navigatorsMap:HashMap<Int ,Navigator?> = hashMapOf()
    private val tabNavigatorMap:HashMap<String ,Navigator?> = hashMapOf()

    @OptIn(InternalVoyagerApi::class)
    fun addTabNavigator(navigator: Navigator?){
        navigator?.let { tabNavigatorMap[it.key]  = navigator }
    }

    fun addLauncherNavigator(navigator: Navigator?){
        navigator?.let { navigatorsMap[it.level] = navigator }
    }

    fun popAllTabs(){
        tabNavigatorMap.keys.forEach {
            tabNavigatorMap[it]?.popAll()
            tabNavigatorMap.remove(it)
        }
    }

    fun resetNavigation(){
        navigatorsMap.keys.reversed().forEach {
            println("hackinoooooo ${ navigatorsMap[it]?.level}")
            println("hackinoooooo ${ navigatorsMap[it]?.lastItem!!::class.simpleName}")
            println("hackinoooooo ${ navigatorsMap[it]?.size}")
            println("hackinoooooo ${ navigatorsMap[it]?.canPop}")
            println("hackinoooooo -------- --------- --------- --------- -------")
            navigatorsMap[it]?.popUntilRoot()

        }
        navigatorsMap.clear()
        tabNavigatorMap.clear()
    }
}



@Preview
@Composable
fun App() {
    KoinContext {
        MaterialTheme {
            Navigator(screen = AppScreenLauncher()){
                SlideTransition(it)
            }
        }
    }
}
class AppScreenLauncher: Screen {

    @Composable
    override fun Content() {
        AppNavigator.addLauncherNavigator( LocalNavigator.current)
        RoutingScreen(interaction = object : RoutingScreenInteraction {
            override fun getAppState(): AppState  = SharedPref.appState
        })
    }

    @Composable
    fun RoutingScreen(interaction: RoutingScreenInteraction? = null) {
            val launcher = when (interaction?.getAppState()) {
                AppState.NONE -> RegistrationLauncher()
                AppState.MAIN_ACTIVITY -> MainScreenLauncher()
                else -> RegistrationLauncher()
            }
            LocalNavigator.current?.push(launcher)
    }

    interface RoutingScreenInteraction{
        fun getAppState():AppState
    }
}

