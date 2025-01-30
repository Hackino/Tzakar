package com.senior25.tzakar.platform_specific.log

import platform.Foundation.NSLog

actual fun LoggingError(tag: String, message: String?) {
        NSLog("iOS log message")
    NSLog("tag(${tag})  ${message.toString()}")
}