package com.senior25.tzakar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import tzakar_reminder.composeapp.generated.resources.Res
import tzakar_reminder.composeapp.generated.resources.ic_email
import tzakar_reminder.composeapp.generated.resources.ic_eye_off
import tzakar_reminder.composeapp.generated.resources.ic_eye_on

@Preview
@Composable
fun App() {
    MaterialTheme {
        Scaffold (backgroundColor = MyColors.colorWhite) {
            Column {
                Image(painter = painterResource(Res.drawable.ic_email),"")
                Image(painter = painterResource(Res.drawable.ic_eye_on),"")
                Image(painter = painterResource(Res.drawable.ic_eye_off),"")


            }

        }
    }
}
