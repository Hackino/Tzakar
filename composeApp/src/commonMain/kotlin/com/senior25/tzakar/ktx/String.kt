package com.senior25.tzakar.ktx

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
    Json.decodeFromString<T?>(this.replace("<null>","{}"))
}?:default

inline fun <reified T> T.encodeToJson()  = Json.encodeToString(this)

fun String.fixAvatarDbJson() =
    this.replace(Regex("(\\w+)="), "\"$1\":") // Replace '=' with ':'
        .replace(Regex("\\[([^\\]]+)\\]")) { matchResult ->
            val items = matchResult.groupValues[1].split(",").map { it.trim() }
            "[" + items.joinToString(", ") { "\"$it\"" } + "]"
        }.replace(Regex(",\\s*(?=\\w)"), ", ")


fun String.fixStringObjectJson() =
    this.replace("=", ":")  // Convert `=` to `:` for JSON format
        .replace(Regex("(\\w+):")) { matchResult ->  // Ensure keys are wrapped in double quotes
            val key = matchResult.groupValues[1]
            "\"$key\":"
        }
        .replace(Regex("([\\w-]+):")) { matchResult ->  // Ensure object keys are quoted
            val key = matchResult.groupValues[1]
            if (key.contains("-") || key.contains(":")) "\"$key\":" else key + ":"
        }
        .replace(":", ": ")  // Add space after colons for readability
