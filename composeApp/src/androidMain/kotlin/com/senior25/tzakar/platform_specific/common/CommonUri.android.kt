package com.senior25.tzakar.platform_specific.common

import android.net.Uri

class AndroidCommonUri : CommonUri {
     override fun parse(uriString: String): String {
        val uri = Uri.parse(uriString)
        return uri.toString()  // Returning the parsed URI as a string
    }

    override fun encode(uri: String): String {
        return Uri.encode(uri)  // Android's URI encoding
    }

    override fun decode(uri: String): String {
        return Uri.decode(uri)  // Android's URI decoding
    }
}

actual fun getUri(): CommonUri = AndroidCommonUri()