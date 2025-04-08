package com.senior25.tzakar.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

object PrefsDataStoreManager {

    private var prefsDataStore: PrefsDataStore? = null

    fun getSharedPreference(): PrefsDataStore {
        if (prefsDataStore == null) {
            prefsDataStore = getSharedPref()
        }
        return prefsDataStore!!
    }

    fun observePrefBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        val dataStoreKey = booleanPreferencesKey(key)
        return getSharedPreference().data
            .map { preferences -> preferences[dataStoreKey] ?: defaultValue }
    }
}

typealias PrefsDataStore = DataStore<Preferences>

fun createDataStore(producePath: () -> String): PrefsDataStore =
    PreferenceDataStoreFactory.createWithPath(produceFile = { producePath().toPath() })

const val dataStoreFileName = "cmp.preferences_pb"

expect fun getSharedPref(): PrefsDataStore