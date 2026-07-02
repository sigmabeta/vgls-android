package com.vgleadsheets.database.source

import kotlinx.coroutines.flow.Flow

interface AlternateSettingDataSource {
    suspend fun toggleAlternate(id: Long)

    fun isAlternateSelected(id: Long): Flow<Boolean>
}
