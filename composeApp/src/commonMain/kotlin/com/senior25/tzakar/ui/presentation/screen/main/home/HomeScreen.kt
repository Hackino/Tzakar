package com.senior25.tzakar.ui.presentation.screen.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfileViewModel

object HomeTab: Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Home)
            val title  = "Home"
            val index:UShort= 1u
            return TabOptions(icon = icon,title = title, index = index)
        }

    @Composable
    override fun Content() {
        Navigator(HomeScreen()){  SlideTransition(it) }
    }
}

class HomeScreen: Screen {
    @Composable
    override fun Content() {
        AppNavigator.addTabNavigator(LocalNavigator.current)
        val screenModel = koinScreenModel<HomeScreenViewModel>()

        val navigator = LocalNavigator.currentOrThrow
        val mainViewModel = koinScreenModel<MainScreenViewModel>()
        Scaffold {padding-> Column {} }
    }
}
