package com.senior25.tzakar.ui.presentation.screen.main.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.app.AppNavigator
import com.senior25.tzakar.ui.presentation.components.image.LoadMediaImage
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.profile.NavigationAction
import com.senior25.tzakar.ui.theme.MyColors
import com.senior25.tzakar.ui.theme.fontH2
import com.senior25.tzakar.ui.theme.fontParagraphM
import kotlinx.coroutines.flow.StateFlow
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.ic_profile_placeholder

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
        val interaction  = object : HomeScreenInteraction {
            override fun getUiState(): StateFlow<HomePageUiState?> = screenModel.uiState
            override fun getProfileState(): StateFlow<UserProfile?> = mainViewModel.userProfile
            override fun getCurrentDate(): String? = mainViewModel.getCurrentDate()
            override fun onUIEvent(event: HomePageEvent) { screenModel.onUIEvent(event) }
            override fun navigate(action: NavigationAction) {}
        }
        Scaffold(
            backgroundColor = MyColors.colorOffWhite,
            topBar = {
                TopAppBar(
                    elevation = 0.dp,
                    title = {
                        Column {
                            interaction.getCurrentDate()?.let {
                                Text(
                                    it,
                                    textAlign =  TextAlign.Start,
                                    color = MyColors.colorDarkBlue,
                                    style = fontParagraphM,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            Text(
                                "Hello, ${interaction.getProfileState().value?.userName?:""}",
                                textAlign =  TextAlign.Start,
                                color = MyColors.colorDarkBlue,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = fontH2,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    actions = {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            LoadMediaImage(
                                modifier = Modifier
                                    .size(50.dp)
                                    .background(Color.White, CircleShape)
                                    .clip(CircleShape),
                                url =interaction.getProfileState().value?.image,
                                default = Res.drawable.ic_profile_placeholder
                            )
                        }

                    },
                    backgroundColor = MyColors.colorOffWhite
                )
            },
            content = {
                HomeScreen(interaction)
            }
        )
    }

    @Composable
    fun HomeScreen(interaction: HomeScreenInteraction){

    }

    interface HomeScreenInteraction{
        fun getUiState(): StateFlow<HomePageUiState?>
        fun onUIEvent(event: HomePageEvent)
        fun navigate(action: NavigationAction)
        fun getProfileState(): StateFlow<UserProfile?>
        fun getCurrentDate(): String?

    }

}


