package com.senior25.tzakar.ui.presentation.screen.main._page

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.ktx.koinNavigatorScreenModel
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBarBack
import com.senior25.tzakar.ui.presentation.screen.main.home.HomeScreen

data class MainScreenLauncher(val test:String? = null):Screen {
    @Composable
    override fun Content() {
        Navigator(screen =  MainScreen(test=test))
    }
}

data class MainScreen(val test:String? = null):Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinNavigatorScreenModel<MainScreenViewModel>()
        Scaffold(
            topBar = {
                MyTopAppBarBack("mainPage", interaction = object : BackPressInteraction {
                    override fun onBackPress() {
                        navigator.pop()
                    }
                })
            }
        ) { padding ->
            screenModel.testCount = 1
            Navigator(
               screen =  HomeScreen(),
            ) { navigator ->
                SlideTransition(navigator)
            }
        }


    }
}