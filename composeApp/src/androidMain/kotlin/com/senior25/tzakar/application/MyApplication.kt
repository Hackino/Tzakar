package com.senior25.tzakar.application

import android.app.Application
import com.google.firebase.FirebaseApp
import com.senior25.tzakar.di.initializeKoin
import com.senior25.tzakar.helper.ApplicationProvider
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin {
            androidContext(this@MyApplication)
        }
        Firebase.initialize(this)
        ApplicationProvider.init(this)
    }
}