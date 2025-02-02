package com.senior25.tzakar.ui.presentation.screen.main.calendar
//
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
//import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ktx.koinParentScreenModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.toast_helper.showToast
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBarBack
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreen
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel

class CalendarScreen:Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator?.koinParentScreenModel<MainScreenViewModel>(
            parentName = MainScreen::class.simpleName
        )?:koinScreenModel()
        showToast(screenModel.testCount.toString())
 Scaffold(
            topBar = {
                MyTopAppBarBack("Calendar", interaction =object : BackPressInteraction {
                    override fun onBackPress() {
                        navigator.pop()
                    }
                })
            }
        ) {padding->

        }
    }
}
