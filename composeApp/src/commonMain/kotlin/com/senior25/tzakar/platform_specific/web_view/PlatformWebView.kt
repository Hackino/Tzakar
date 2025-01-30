package com.senior25.tzakar.platform_specific.web_view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun HtmlWebView(
    htmlContent: String,
    modifier: Modifier = Modifier
)