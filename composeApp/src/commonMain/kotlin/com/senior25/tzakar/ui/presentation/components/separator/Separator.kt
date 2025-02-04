package com.senior25.tzakar.ui.presentation.components.separator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.senior25.tzakar.ui.theme.MyColors

@Composable
fun Separator(color: Color = MyColors.colorLightDarkBlue) {
    Box(modifier = Modifier.fillMaxWidth()
        .height(1.dp)
        .background(color)
    )
}