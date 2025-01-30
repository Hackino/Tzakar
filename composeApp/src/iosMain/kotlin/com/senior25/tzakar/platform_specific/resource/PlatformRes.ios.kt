package com.senior25.tzakar.platform_specific.resource

import com.senior25.tzakar.platform_specific.log.LoggingError
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
actual fun getRawResourceHtmlContent(path: String): String {
    val bundle = NSBundle.mainBundle()
    LoggingError("hackinooo",path + " ----> Received")

    val filePath = bundle.pathForResource(path, "html")
    return if (filePath != null) {
        LoggingError("hackinoooPPATH",filePath.toString())

        val fileContent = NSString.stringWithContentsOfFile(filePath, encoding = NSUTF8StringEncoding, error = null)
        LoggingError("hackinooo",fileContent.toString())
        fileContent?.toString() ?: ""
    } else {
        ""
    }
}