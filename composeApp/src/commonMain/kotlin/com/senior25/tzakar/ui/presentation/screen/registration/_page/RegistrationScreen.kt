package com.senior25.tzakar.ui.presentation.screen.registration._page

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import com.senior25.tzakar.RoutingScreenInteraction
import com.senior25.tzakar.ui.presentation.screen.registration.sign_in.SignInScreen
import com.senior25.tzakar.ui.theme.MyColors
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview
@Composable
fun RegistrationScreen(interaction: RoutingScreenInteraction? = null) {
    Scaffold (backgroundColor = MyColors.colorWhite) {
        SignInScreen()
    }
}
