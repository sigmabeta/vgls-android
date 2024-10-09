package com.vgleadsheets.storage.common

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AndroidDataStore(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
    private val hatchet: Hatchet,
) : Storage {
    override fun saveString(key: String, value: String) {
        coroutineScope.launch(dispatchers.disk) {
            hatchet.v("Saving string to storage: $key -> $value")
            val typedKey = stringPreferencesKey(key)

            dataStore.edit { preferences ->
                preferences[typedKey] = value
            }
        }
    }

    override fun savedStringFlow(key: String): Flow<String?> = dataStore
        .data
        .map { preferences -> preferences[stringPreferencesKey(key)] }

    override fun saveInt(key: String, value: Int) {
        coroutineScope.launch(dispatchers.disk) {
            hatchet.v("Saving integer to storage: $key -> $value")
            val typedKey = intPreferencesKey(key)

            dataStore.edit { preferences ->
                preferences[typedKey] = value
            }
        }
    }

    override fun savedIntFlow(key: String) = dataStore
        .data
        .map { preferences -> preferences[intPreferencesKey(key)] }
}
