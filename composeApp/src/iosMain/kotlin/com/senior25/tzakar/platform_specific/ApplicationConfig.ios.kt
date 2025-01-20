package com.senior25.tzakar.platform_specific

import platform.Foundation.NSBundle


class IosApplicationConfig : ApplicationConfig {
    override val id: String =  NSBundle.mainBundle.bundleIdentifier ?: "Unknown"
}


actual fun getApplicationConfig(): ApplicationConfig = IosApplicationConfig()