package com.senior25.tzakar.platform_specific.resource


import com.senior25.tzakar.R
import com.senior25.tzakar.helper.ApplicationProvider
import java.lang.reflect.Field


actual fun getPlatformString(resourceId: String): String {
    val text = try {
        val field: Field = com.senior25.tzakar.R.string::class.java.getField(resourceId)
        val resId = field.getInt(null)
        ApplicationProvider.application.getString(resId)
    } catch (e: NoSuchFieldException) {
        ""
    } catch (e: IllegalAccessException) {
        ""
    }
    return text
}

actual fun getRawResourceHtmlContent(path: String): String {

    return try {
        val field: Field = com.senior25.tzakar.R.raw::class.java.getField(path)
        val resId = field.getInt(null)
        val inputStream = ApplicationProvider.application.resources.openRawResource(resId)
        inputStream.bufferedReader().use { it.readText() }
    } catch (e: NoSuchFieldException) {
        ""
    } catch (e: IllegalAccessException) {
        ""
    }
}