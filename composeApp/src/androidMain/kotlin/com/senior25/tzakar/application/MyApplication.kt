package com.senior25.tzakar.application

import android.app.Application
import com.senior25.tzakar.di.initializeKoin
import com.senior25.tzakar.helper.ApplicationProvider

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidContext(this@MyApplication)
        }
        ApplicationProvider.init(this)
    }
}