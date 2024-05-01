package com.vgleadsheets.environment

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.vgleadsheets.coroutines.VglsDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EnvironmentDataStore(
    private val dataStore: DataStore<Preferences>,
    private val coroutineScope: CoroutineScope,
    private val dispatchers: VglsDispatchers,
) : EnvironmentManager {
    override fun setEnvironment(environment: Environment) {
        coroutineScope.launch(dispatchers.disk) {
            dataStore.edit { preferences ->
                preferences[KEY_SETTING_ENVIRONMENT] = environment.ordinal
            }
        }
    }

    override fun selectedEnvironmentFlow() = dataStore
        .data
        .map { preferences -> preferences[KEY_SETTING_ENVIRONMENT] }
        .map {
            if (it == null) {
                Environment.PROD
            } else if (it < 0 || it >= Environment.entries.size) {
                Environment.PROD
            } else {
                Environment.entries[it]
            }
        }
        .flowOn(dispatchers.disk)

    companion object {
        private const val SETTING_ENVIRONMENT = "setting.debug.environment"
        private val KEY_SETTING_ENVIRONMENT = intPreferencesKey(SETTING_ENVIRONMENT)
    }
}
