package com.senior25.tzakar.platform_specific.web_view

import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun WebView(
    url: String,
    modifier: Modifier
){
    AndroidView(
        modifier = modifier,
        factory = { context ->
            android.webkit.WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(url)
            }
        }
    )
}
