package com.senior25.tzakar.ui.presentation.screen.main._page

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.senior25.tzakar.di.mainScreenViewModelModule
import com.senior25.tzakar.ktx.koinNavigatorScreenModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.exitApp
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.screen.main.calendar.CalendarTab
import com.senior25.tzakar.ui.presentation.screen.main.home.HomeTab
import com.senior25.tzakar.ui.presentation.screen.main.profile.ProfileTab
import com.senior25.tzakar.ui.theme.MyColors
import kotlinx.coroutines.delay
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

data class MainScreenLauncher(val test:String? = null):Screen {


    @Composable
    override fun Content() {
        var isModuleLoaded by remember { mutableStateOf(false) } // Track module load state

        LaunchedEffect(Unit) {
            println("initt moduleeee")
            loadKoinModules(mainScreenViewModelModule)
            isModuleLoaded = true
        }

        DisposableEffect(Unit) {

            onDispose {
                println("killllll moduleeee")
                unloadKoinModules(mainScreenViewModelModule)
                loadKoinModules(mainScreenViewModelModule)

            }
        }

        if (isModuleLoaded) {
            val navigator = LocalNavigator.currentOrThrow
            Navigator(
                screen = MainScreen(),
                onBackPressed = {
                    if (navigator.lastItem::class.simpleName == MainScreenLauncher::class.simpleName) exitApp()
                    true
                },
                key = "MainScreenLauncherNavigator"
            )
        }
    }
}

class MainScreen:Screen {

    override val key: ScreenKey
        get() = "MainScreenKey"

    @Composable
    override fun Content() {
       val mainViewModel = koinScreenModel<MainScreenViewModel>()
        TabNavigator(HomeTab,key ="MainScreenTabNavigator"){
            Scaffold(
                content = {
                    mainViewModel.testCount = 1
                    CurrentTab()
                },
                bottomBar = {
                    BottomNavigation(
                        backgroundColor = MyColors.colorPurple,
                        elevation = 2.dp,
                        modifier = Modifier.clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    ) {
                        TabNavigationItem(HomeTab){
                            mainViewModel.testCount += 1
                        }
                        TabNavigationItem(CalendarTab){
                            mainViewModel.testCount += 1
                        }
                        TabNavigationItem(ProfileTab){
                            mainViewModel.testCount += 1
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab:Tab,onClick:()->Unit){
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = {
            AppNavigator.popAllTabs()
            tabNavigator.current = tab
            onClick()
        },
        label = {
            val isSelected =  tabNavigator.current == tab
            val color =  if (isSelected) MyColors.colorDarkBlue else MyColors.colorWhite
            Text(text =  tab.options.title, color = color)
        },
        alwaysShowLabel = true,
        icon = {
            tab.options.icon?.let {painter->
                val isSelected =  tabNavigator.current == tab
                val color =  if (isSelected) MyColors.colorDarkBlue else MyColors.colorWhite
                Icon(painter = painter, contentDescription = tab.options.title, tint = color)
            }
        }
    )
}