package com.senior25.tzakar.helper

import android.app.Application

object ApplicationProvider {
    lateinit var application: Application
        private set

    fun init(application: Application) {
        this.application = application
    }
}