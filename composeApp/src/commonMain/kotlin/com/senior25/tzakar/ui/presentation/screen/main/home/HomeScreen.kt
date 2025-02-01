package com.senior25.tzakar.ui.presentation.screen.main.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ui.presentation.components.button.CustomButton
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBarBack
import com.senior25.tzakar.ui.presentation.screen.main.profile.ProfileScreen
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInAction
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.ic_sign_in
import tzakar_reminder.composeapp.generated.resources.sign_in

class HomeScreen:Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = {
                MyTopAppBarBack("home", interaction =object : BackPressInteraction {
                    override fun onBackPress() {
                        navigator.pop()
                    }
                })
            }
        ) {padding->
            CustomButton(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                onClick =  { navigator.push(ProfileScreen()) },
                isEnabled = true,
                text ="navigate to profile"
            )
        }
    }
}
