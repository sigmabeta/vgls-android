package com.vgleadsheets.database.source

import com.vgleadsheets.model.history.ComposerPlayCount
import kotlinx.coroutines.flow.Flow

interface ComposerPlayCountDataSource {
    suspend fun incrementPlayCount(composerId: Long)

    fun getAll(): Flow<List<ComposerPlayCount>>
}
