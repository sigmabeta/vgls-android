package com.vgleadsheets.environment

import kotlinx.coroutines.flow.Flow

interface EnvironmentManager {
    fun setEnvironment(environment: Environment)

    fun selectedEnvironmentFlow(): Flow<Environment>
}
