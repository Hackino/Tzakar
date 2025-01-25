package com.senior25.tzakar.platform_specific.toast_helper

import android.widget.Toast
import com.senior25.tzakar.helper.ApplicationProvider

actual fun showToast(message: String) {
    Toast.makeText(ApplicationProvider.application, message, Toast.LENGTH_SHORT).show()
}