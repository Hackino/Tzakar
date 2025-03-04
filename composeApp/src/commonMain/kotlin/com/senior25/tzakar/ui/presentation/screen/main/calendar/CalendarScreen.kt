package com.senior25.tzakar.ui.presentation.screen.main.calendar
//
//import cafe.adriel.voyager.koin.koinNavigatorScreenModel
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.ktx.koinParentScreenModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.platform_specific.toast_helper.showToast
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreen
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.edit_profile.EditProfileViewModel
import com.senior25.tzakar.ui.presentation.screen.main.profile.ProfileScreen

object CalendarTab: Tab {

    override val options: TabOptions
        @Composable
        get() {
            val icon = rememberVectorPainter(Icons.Default.DateRange)
            val title  = "Calendar"
            val index:UShort= 1u
            return TabOptions(icon = icon,title = title, index = index)
        }
    @Composable
    override fun Content() {
        Navigator(CalendarScreen()){ navigator: Navigator ->
            SlideTransition(navigator)
        }
    }
}

class CalendarScreen: Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = koinScreenModel<EditProfileViewModel>()
        val mainViewModel = koinScreenModel<MainScreenViewModel>()
        Scaffold(
        ) {padding->

        }
    }
}
