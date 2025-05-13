package com.senior25.tzakar.ui.presentation.screen.web

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLFile
import com.senior25.tzakar.platform_specific.getPlatform
import com.senior25.tzakar.platform_specific.resource.getRawResourceHtmlContent
import com.senior25.tzakar.platform_specific.web_view.HtmlWebView
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBar

data class WebViewScreen(val title: String? = null,val link: String? = null):Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val webViewState = rememberWebViewStateWithHTMLFile(fileName = "$link.html")
        Scaffold(
            topBar = {
                MyTopAppBar(title?:"", interaction =object : BackPressInteraction {
                    override fun onBackPress() {
                        navigator.pop()
                    }
                })
            }
        ) {padding->
            Box(
                modifier = Modifier.fillMaxSize().navigationBarsPadding()
            ) {


                if (getPlatform().name.contains("Android"))
                    HtmlWebView(
                        getRawResourceHtmlContent(link ?: ""),
                        modifier = Modifier.fillMaxWidth(),
                    )
                else {
                    WebView(
                        state = webViewState,
                        modifier = Modifier.fillMaxSize(),
                        captureBackPresses = false,
                    )
                }
            }
        }
    }
}
