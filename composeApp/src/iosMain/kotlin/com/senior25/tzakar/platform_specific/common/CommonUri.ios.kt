package com.senior25.tzakar.platform_specific.common

import com.senior25.tzakar.platform_specific.ApplicationConfig
import platform.Foundation.NSURL


class IosCommonUri : CommonUri {
    override fun parse(uriString: String): String {
        val uri = NSURL(string = uriString)
        return uri?.absoluteString ?: ""  // Returning the URI as a string
    }

    override fun encode(uri: String): String {
        return uri
    }

    override fun decode(uri: String): String {
        return uri  // iOS doesn't need specific decoding here either
    }
}

actual fun getUri(): CommonUri = IosCommonUri()