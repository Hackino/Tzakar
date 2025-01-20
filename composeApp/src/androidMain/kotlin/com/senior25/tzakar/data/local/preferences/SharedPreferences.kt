package com.senior25.tzakar.data.local.preferences

import com.senior25.tzakar.helper.ApplicationProvider

actual fun getSharedPref(): PrefsDataStore {
    return createDataStore {
        ApplicationProvider.application.filesDir.resolve(dataStoreFileName).absolutePath
    }
}