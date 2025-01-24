package com.senior25.tzakar.ui.presentation.screen.registration.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.resources.painterResource
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.app_icon
import tzakar_reminder.composeapp.generated.resources.ic_google

@Composable
fun SignInScreen() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MyColors.colorWhite)
            .padding(vertical = 16.dp),
    ) {
        Column(
            modifier =  Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource(Res.drawable.app_icon),
                contentDescription =  ""
            )
        }
    }
}