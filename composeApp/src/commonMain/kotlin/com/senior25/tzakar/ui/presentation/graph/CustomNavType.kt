package com.senior25.tzakar.ui.presentation.graph

import androidx.core.bundle.Bundle
import androidx.navigation.NavType
import com.senior25.tzakar.platform_specific.common.CommonUri
import com.senior25.tzakar.platform_specific.common.getUri
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object CustomNavType {
    inline fun <reified T> navType() = object : NavType<T>(isNullableAllowed = true) {
        override fun get(bundle: Bundle, key: String): T? {
            val value = bundle.getString(key)
            return if (value != null) Json.decodeFromString(value) else null
        }

        override fun parseValue(value: String): T {
            return Json.decodeFromString(getUri().decode(value))
        }

        override fun serializeAsValue(value: T): String {
            return getUri().encode(Json.encodeToString(value))
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}