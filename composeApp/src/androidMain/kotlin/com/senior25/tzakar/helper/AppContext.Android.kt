package com.senior25.tzakar.helper

actual fun getApplicationContext(): Any? {
    return ApplicationProvider.application.applicationContext
}

