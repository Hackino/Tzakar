package com.senior25.tzakar.ui.presentation.screen.web

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.senior25.tzakar.platform_specific.web_view.WebView
import com.senior25.tzakar.ui.presentation.components.toolbar.BackPressInteraction
import com.senior25.tzakar.ui.presentation.components.toolbar.MyTopAppBarBack

@Composable
fun WebViewScreen(navController: NavHostController? = null, title: String? = null, link: String? = null) {
    Scaffold(
        topBar = {
            MyTopAppBarBack(title?:"", interaction =object : BackPressInteraction {
                override fun onBackPress() {
                    navController?.navigateUp()
                }
            })
        }
    ) {padding->
        WebView(
            link?:"",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
