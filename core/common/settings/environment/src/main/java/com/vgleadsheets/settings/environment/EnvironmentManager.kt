package com.vgleadsheets.settings.environment

import com.vgleadsheets.settings.common.Storage
import kotlinx.coroutines.flow.map

class EnvironmentManager(
    private val storage: Storage
) {
    fun setEnvironment(environment: Environment) {
        storage.saveInt(SETTING_ENVIRONMENT, environment.ordinal)
    }

    fun selectedEnvironmentFlow() = storage.savedIntFlow(SETTING_ENVIRONMENT)
        .map {
            if (it == null) {
                ENVIRONMENT_DEFAULT
            } else if (it < 0 || it >= Environment.entries.size) {
                ENVIRONMENT_DEFAULT
            } else {
                Environment.entries[it]
            }
        }

    companion object {
        private val ENVIRONMENT_DEFAULT = Environment.PROD
        private const val SETTING_ENVIRONMENT = "setting.debug.environment"
    }
}
