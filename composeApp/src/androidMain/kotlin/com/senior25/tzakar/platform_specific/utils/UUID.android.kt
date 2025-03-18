package com.senior25.tzakar.platform_specific.utils

import java.util.UUID

actual fun generateUUID(): String {
    return UUID.randomUUID().toString()
}