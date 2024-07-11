package com.vgleadsheets.database.source

import com.vgleadsheets.model.history.SongHistoryEntry
import kotlinx.coroutines.flow.Flow

interface SongHistoryDataSource {
    suspend fun insert(model: SongHistoryEntry)

    fun getAll(): Flow<List<SongHistoryEntry>>
}
