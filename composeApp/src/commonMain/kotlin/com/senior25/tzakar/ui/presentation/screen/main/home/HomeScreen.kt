package com.senior25.tzakar.ui.presentation.screen.main.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import cafe.adriel.voyager.transitions.SlideTransition
import com.senior25.tzakar.data.local.model.profile.UserProfile
import com.senior25.tzakar.data.local.preferences.SharedPref
import com.senior25.tzakar.helper.DataBaseReference
import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.koinParentScreenModel
import com.senior25.tzakar.ktx.koinScreenModel
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreen
import com.senior25.tzakar.ui.presentation.screen.main._page.MainScreenViewModel
import com.senior25.tzakar.ui.presentation.screen.main.calendar.CalendarScreen
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.database.database
import io.ktor.util.encodeBase64
import kotlinx.coroutines.flow.first

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
        Navigator(HomeScreen()){ navigator: Navigator ->
            SlideTransition(navigator)
        }
    }
}


class  HomeScreen: Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.koinParentScreenModel<MainScreenViewModel>(
            parentName = MainScreen::class.simpleName
        )?:koinScreenModel()

        var user by remember { mutableStateOf(UserProfile()) }

        LaunchedEffect(Unit){
            SharedPref.loggedInEmail?.let {
                user = Firebase.database.reference(DataBaseReference.UserProfiles.reference)
                    .child(it.encodeBase64()).valueEvents.first().value.toString().decodeJson(UserProfile())?:UserProfile()
            }
        }
        Scaffold {padding->
            Column {
                CustomButton(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    onClick =  {
                        screenModel.testCount = screenModel.testCount.plus(1)
//                        navigator.push(ProfileScreen)
                    },
                    isEnabled = true,
                    text ="navigate to profile"
                )
                user.let {
                    Text(text = user.email?:"",color = Color.Black)
                    Divider(modifier = Modifier.fillMaxWidth(), color = Color.Black, thickness = 2.dp)
                    Text(text = user.id?:"",color = Color.Black)
                    Divider(modifier = Modifier.fillMaxWidth(), color = Color.Black, thickness = 2.dp)
                    Text(text = user.googleToken?:"",color = Color.Black)
                }
            }

        }
    }
}
