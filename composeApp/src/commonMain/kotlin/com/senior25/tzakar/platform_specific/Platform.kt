package com.senior25.tzakar.platform_specific

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform