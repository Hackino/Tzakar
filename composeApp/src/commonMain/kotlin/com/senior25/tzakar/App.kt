package com.senior25.tzakar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.senior25.tzakar.data.local.preferences.SharedPref
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        SharedPref.selectedLanguage = "ar"
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(SharedPref.selectedLanguage)

        }
    }
}