package com.senior25.tzakar.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey

import com.senior25.tzakar.ktx.decodeJson
import com.senior25.tzakar.ktx.encodeToJson
import com.senior25.tzakar.ktx.removeDoubleQuotes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object PreferenceHelper {

    inline fun <reified T> DataStore<Preferences>.saveToDataStore(key:String, value: T) {
        runBlocking{
            edit { preferences -> preferences[stringPreferencesKey(key)] = value.encodeToJson() }
        }
    }

    inline fun <reified T> DataStore<Preferences>.getFromDataStore(key: String, default: T? = null): T? {
        return runBlocking {
            val encryptedString = data.first()[stringPreferencesKey(key)] ?: return@runBlocking default
            val result = try {
                when (T::class) {
                    String::class -> encryptedString.decodeJson(default)
                    else -> encryptedString.removeDoubleQuotes().replace("\\\"", "\"").decodeJson(default)
                }
            }catch (e:Exception){ default }
            return@runBlocking result
        }
    }



    fun DataStore<Preferences>.clearPreferencesDataStore() {
        CoroutineScope(Dispatchers.IO).launch { edit { preferences -> preferences.clear() } }
    }
}
