package com.senior25.tzakar.platform_specific

interface ApplicationConfig {
    val id: String
}

expect fun getApplicationConfig(): ApplicationConfig
