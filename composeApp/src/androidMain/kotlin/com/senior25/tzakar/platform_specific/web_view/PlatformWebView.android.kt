package com.senior25.tzakar.platform_specific.web_view

import android.util.Log
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.senior25.tzakar.R

@Composable
actual fun HtmlWebView(
    htmlContent: String,
    modifier: Modifier
){
    AndroidView(
        modifier = modifier,
        factory = { context ->
            android.webkit.WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                Log.e("hackinooooo","${url}")

                loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)

            }
        }
    )
}
