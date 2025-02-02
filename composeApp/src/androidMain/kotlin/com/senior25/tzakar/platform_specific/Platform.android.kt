package com.senior25.tzakar.platform_specific

import android.os.Build
import com.senior25.tzakar.MainActivity


class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun exitApp() {
    MainActivity.getCurrentActivity()?.finish()
}