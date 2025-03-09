package com.senior25.tzakar.platform_specific.common

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

actual fun getSystemTimeMillis(): Long  =System.currentTimeMillis()


actual fun getCurrentDateFormatted(): String {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    return currentDate.format(formatter)
}