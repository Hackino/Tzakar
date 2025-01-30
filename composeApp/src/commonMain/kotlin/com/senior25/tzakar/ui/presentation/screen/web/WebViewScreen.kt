package com.senior25.tzakar.ui.presentation.screen.web

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewStateWithHTMLFile
import com.senior25.tzakar.platform_specific.getPlatform
import com.senior25.tzakar.platform_specific.resource.getRawResourceHtmlContent
import com.senior25.tzakar.platform_specific.web_view.HtmlWebView
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBarBack

@Composable
fun WebViewScreen(navController: NavHostController? = null, title: String? = null, link: String? = null) {
    val webViewState = rememberWebViewStateWithHTMLFile(fileName = "$link.html")
    Scaffold(
        topBar = {
            MyTopAppBarBack(title?:"", interaction =object : BackPressInteraction {
                override fun onBackPress() {
                    navController?.navigateUp()
                }
            })
        }
    ) {padding->
        if (getPlatform().name.contains("Android") )
            HtmlWebView(
                getRawResourceHtmlContent(link?:""),
                modifier = Modifier.fillMaxWidth(),
            )
        else{
            WebView(
                state = webViewState,
                modifier = Modifier.fillMaxSize(),
                captureBackPresses = false,
            )
        }


    }
}
