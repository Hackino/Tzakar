package com.senior25.tzakar.platform_specific.common

interface CommonUri {
    fun parse(uriString: String): String
    fun encode(uri: String): String
    fun decode(uri: String): String
}

expect fun getUri(): CommonUri