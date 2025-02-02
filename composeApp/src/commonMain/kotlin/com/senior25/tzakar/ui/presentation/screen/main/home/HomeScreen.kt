package com.senior25.tzakar.ui.presentation.screen.main.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
//import cafe.adriel.voyager.koin.getNavigatorScreenModel
//import cafe.adriel.voyager.koin.koinNavigatorScreenModel
//import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ktx.koinNavigatorScreenModel
import com.senior25.tzakar.ktx.koinParentScreenModel
import com.senior25.tzakar.ktx.koinScreenModel
//import com.senior25.tzakar.ktx.getNavigatorScreenModel
//import com.senior25.tzakar.ktx.getScreenModel
import com.senior25.tzakar.platform_specific.log.LoggingError
import com.senior25.tzakar.platform_specific.toast_helper.showToast
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBarBack
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreen
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.profile.ProfileScreen

class HomeScreen:Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator?.koinParentScreenModel<MainScreenViewModel>(
            parentName = MainScreen::class.simpleName
        )?:koinScreenModel()

        Scaffold(
            topBar = {
                MyTopAppBarBack("home", interaction =object : BackPressInteraction {
                    override fun onBackPress() { navigator.pop() }
                })
            }
        ) {padding->
            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                onClick =  {
                    screenModel.testCount = screenModel.testCount.plus(1)
                    navigator.push(ProfileScreen())
                           },
                isEnabled = true,
                text ="navigate to profile"
            )
        }
    }
}
