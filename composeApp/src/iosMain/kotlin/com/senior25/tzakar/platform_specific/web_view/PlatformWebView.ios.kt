package com.senior25.tzakar.platform_specific.web_view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.WKWebView
import platform.WebKit.WKWebViewConfiguration


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebView(
    url: String,
    modifier: Modifier
){
    UIKitView(
        modifier = modifier,
        factory = {
            val configuration = WKWebViewConfiguration()
            val webView = WKWebView(frame = CGRectZero.readValue(), configuration = configuration)

            // Load the URL
            val nsUrl = NSURL(string = url)
            webView.loadRequest(NSURLRequest(nsUrl))

            webView
        }
    )
}
