package com.senior25.tzakar.helper.encode



 fun String.encodeUrl(): String {
    return map {
        when (it) {
            ' ' -> "%20"
            '#' -> "%23"
            '%' -> "%25"
            '&' -> "%26"
            '\'' -> "%27"
            '(' -> "%28"
            ')' -> "%29"
            '*' -> "%2A"
            '+' -> "%2B"
            ',' -> "%2C"
            '/' -> "%2F"
            ':' -> "%3A"
            ';' -> "%3B"
            '=' -> "%3D"
            '?' -> "%3F"
            '@' -> "%40"
            '[' -> "%5B"
            ']' -> "%5D"
            else -> it.toString()
        }
    }.joinToString("")
}

fun String.decodeUrl(): String {
    return replace("%20", " ")
        .replace("%23", "#")
        .replace("%25", "%")
        .replace("%26", "&")
        .replace("%27", "'")
        .replace("%28", "(")
        .replace("%29", ")")
        .replace("%2A", "*")
        .replace("%2B", "+")
        .replace("%2C", ",")
        .replace("%2F", "/")
        .replace("%3A", ":")
        .replace("%3B", ";")
        .replace("%3D", "=")
        .replace("%3F", "?")
        .replace("%40", "@")
        .replace("%5B", "[")
        .replace("%5D", "]")
}