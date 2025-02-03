package com.senior25.tzakar.ui.presentation.screen.main.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfileScreen

object ProfileTab: Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.Person)
            val title  = "Profile"
            val index:UShort= 1u
            return TabOptions(icon = icon,title = title, index = index)
        }

    @Composable
    override fun Content() {
        Navigator(ProfileScreen()){navigator: Navigator ->
            SlideTransition(navigator)
        }
    }
}


class ProfileScreen: Screen {


    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
//        val screenModel = navigator?.koinParentScreenModel<MainScreenViewModel>(
//            parentName = MainScreen::class.simpleName
//        )?:koinScreenModel()
        Scaffold {padding->
            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                onClick =  {
                    navigator?.push(EditProfileScreen())
//                    screenModel.testCount = screenModel.testCount.plus(1)
                },
                isEnabled = true,
                text ="navigate to calendar"
            )
        }
    }
}
