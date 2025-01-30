package com.senior25.tzakar.platform_specific.log

import android.util.Log

actual fun LoggingError(tag: String, message: String?) {
    Log.e(tag,message.toString())
}