package com.vgleadsheets.database.source

import com.vgleadsheets.model.updates.OfflineUpdateResult
import kotlinx.coroutines.flow.Flow

interface OfflineUpdateResultDataSource {
    suspend fun insert(entity: OfflineUpdateResult)
    fun getAll(): Flow<List<OfflineUpdateResult>>
    fun nukeTable()
}
