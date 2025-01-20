package com.senior25.tzakar.ktx

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.String

inline fun String.ifEmpty(default:()-> String?): String?{
    return if (isEmpty()) default()  else this
}


fun String?.removeDoubleQuotes(): String {
    if (isNullOrEmpty()) return ""
    var outputText = this
    if (outputText.startsWith("\""))  outputText = outputText.substring(1)
    if (outputText.endsWith("\"")) {
        outputText = outputText.substring(0, outputText.length - 1)
    }
    if (outputText.startsWith("\"") || outputText.endsWith("\"")) {
        outputText.removeDoubleQuotes()
    }
    return outputText
}


inline fun <reified T> String.decodeJson(default:T? = null): T? = this?.let{
    Json.decodeFromString<T?>(this)
}?:default

inline fun <reified T> T.encodeToJson()  = Json.encodeToString(this)


