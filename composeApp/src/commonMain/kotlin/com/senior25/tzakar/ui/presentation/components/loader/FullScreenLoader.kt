package com.senior25.tzakar.ui.presentation.components.loader

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.ui.theme.MyColors


@Composable
fun FullScreenLoader() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .clickable(
                enabled = true,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {},
            )
        ,
        color = Color.Transparent
    ) {
        Box(modifier = Modifier.fillMaxSize().background(MyColors.colorBlack.copy(alpha = 0.1f))) {
            Column (
                modifier = Modifier.fillMaxSize().background(Color.Transparent),
                verticalArrangement = Arrangement.Center
            ) {
                LoaderWidget( Modifier.fillMaxWidth().size(80.dp))
            }
        }
    }
}
