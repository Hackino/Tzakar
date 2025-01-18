package com.senior25.tzakar

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform