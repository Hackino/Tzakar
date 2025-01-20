package com.senior25.tzakar.platform_specific


class AndroidApplicationConfig : ApplicationConfig {
    override val id: String = ""
}

actual fun getApplicationConfig(): ApplicationConfig = AndroidApplicationConfig()