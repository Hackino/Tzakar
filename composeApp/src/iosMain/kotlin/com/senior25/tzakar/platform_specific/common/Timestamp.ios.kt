package com.senior25.tzakar.platform_specific.common

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.timeIntervalSince1970

actual fun getSystemTimeMillis(): Long = NSDate().timeIntervalSince1970.toLong() * 1000


actual fun getCurrentDateFormatted(): String {
    val date = NSDate()
    val formatter = NSDateFormatter()
    formatter.dateFormat = "MMM d, yyyy"
    return formatter.stringFromDate(date)
}