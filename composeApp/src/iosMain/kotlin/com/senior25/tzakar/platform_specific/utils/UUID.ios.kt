package com.senior25.tzakar.platform_specific.utils

import platform.Foundation.NSUUID

actual fun generateUUID(): String {
    return NSUUID().UUIDString()
}