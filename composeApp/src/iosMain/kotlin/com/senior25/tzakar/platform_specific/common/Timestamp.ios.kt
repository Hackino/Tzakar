package com.senior25.tzakar.platform_specific.common

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual fun getSystemTimeMillis(): Long = NSDate().timeIntervalSince1970.toLong() * 1000