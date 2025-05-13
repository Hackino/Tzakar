package com.senior25.tzakar

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import com.senior25.tzakar.di.permissionsModuleShared
import com.senior25.tzakar.ui.presentation.app.App
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {

    companion object {
        private var currentActivityRef: WeakReference<Activity?> = WeakReference(null)
        fun getCurrentActivity(): Activity? = currentActivityRef.get()
        fun setCurrentActivity(activity: Activity?) {
            currentActivityRef = WeakReference(activity)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(
         listOf(
             module { single<Activity> { this@MainActivity } },
             permissionsModuleShared
         )
        )
        // Set dark icons on the status bar (i.e., black icons)
//        // Set dark status bar icons (i.e., black icons on light background)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.setSystemBarsAppearance(
//                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
//                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//            )
//        } else {
//            @Suppress("DEPRECATION")
//            window.decorView.systemUiVisibility =
//                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }

        installSplashScreen()
        enableEdgeToEdge()
        setContent { App() }
    }


    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(
            listOf(
            module { single<Activity> { this@MainActivity } },
            permissionsModuleShared
            )
        )

    }

    override fun onResume() {
        super.onResume()
        setCurrentActivity(this)
        // Apply status bar icon color AFTER content is set
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun onPause() {
        super.onPause()
        setCurrentActivity(null)
    }
}