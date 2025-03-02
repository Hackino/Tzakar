package com.senior25.tzakar.ui.presentation.screen.main._page

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import com.senior25.tzakar.ktx.koinNavigatorScreenModel
import com.senior25.tzakar.platform_specific.exitApp
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.screen.main.calendar.CalendarTab
import com.senior25.tzakar.ui.presentation.screen.main.home.HomeTab
import com.senior25.tzakar.ui.presentation.screen.main.profile.ProfileTab
import com.senior25.tzakar.ui.theme.MyColors

data class MainScreenLauncher(val test:String? = null):Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Navigator(
            screen = MainScreen(test=test),
            onBackPressed = {
                if (navigator.lastItem::class.simpleName == MainScreenLauncher::class.simpleName) exitApp()
                true
            },
            key = "MainScreenLauncherNavigator"
        )
    }
}

data class MainScreen(val test:String? = null):Screen {

    override val key: ScreenKey
        get() = "MainScreenKey"

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val screenModel = navigator.koinNavigatorScreenModel<MainScreenViewModel>()
        TabNavigator(HomeTab,key ="MainScreenTabNavigator"){
            Scaffold(
                content = { screenModel.testCount = 1;CurrentTab() },
                bottomBar = {
                    BottomNavigation(
                        backgroundColor = MyColors.colorPurple,
                        elevation = 2.dp,
                        modifier = Modifier.clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                    ) {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(CalendarTab)
                        TabNavigationItem(ProfileTab)
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab:Tab){
    val tabNavigator = LocalTabNavigator.current

    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = {
            AppNavigator.popAllTabs()
            tabNavigator.current = tab
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