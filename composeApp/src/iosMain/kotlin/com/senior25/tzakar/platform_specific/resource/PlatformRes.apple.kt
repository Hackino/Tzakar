package com.senior25.tzakar.platform_specific.resource

import platform.Foundation.NSBundle

actual fun getPlatformString(resourceId: String): String {
    val nsBundle = NSBundle.mainBundle
    return nsBundle.localizedStringForKey(resourceId, null,null)
}