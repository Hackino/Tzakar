package com.senior25.tzakar.ui.presentation.screen.main.calendar

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBarBack

class CalendarScreen:Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(
            topBar = {
                MyTopAppBarBack("Calendar", interaction =object : BackPressInteraction {
                    override fun onBackPress() {
                        navigator.pop()
                    }
                })
            }
        ) {padding->

        }
    }
}
