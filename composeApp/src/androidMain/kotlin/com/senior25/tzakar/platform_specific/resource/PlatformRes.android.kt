package com.senior25.tzakar.platform_specific.resource

import com.senior25.tzakar.R
import com.senior25.tzakar.helper.ApplicationProvider
import java.lang.reflect.Field

actual fun getPlatformString(resourceId: String): String {
    val text = try {
        val field: Field = R.string::class.java.getField(resourceId)
        val resId = field.getInt(null)
        ApplicationProvider.application.getString(resId)
    } catch (e: NoSuchFieldException) {
        ""
    } catch (e: IllegalAccessException) {
        ""
    }

    return text
}