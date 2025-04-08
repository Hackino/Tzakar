package com.senior25.tzakar

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
        installSplashScreen()
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
    }

    override fun onPause() {
        super.onPause()
        setCurrentActivity(null)
    }
}