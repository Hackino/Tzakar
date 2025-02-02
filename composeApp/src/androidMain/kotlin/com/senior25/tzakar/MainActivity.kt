package com.senior25.tzakar

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.senior25.tzakar.application.MyApplication
import com.senior25.tzakar.ui.presentation.app.App
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
        installSplashScreen()
        setContent { App() }
    }

    override fun onResume() {
        super.onResume()
        setCurrentActivity(this)
    }

    override fun onPause() {
        super.onPause()
        setCurrentActivity(null)
    }
}