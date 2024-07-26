package com.vgleadsheets.database.source

import com.vgleadsheets.model.history.SearchHistoryEntry
import kotlinx.coroutines.flow.Flow

interface SearchHistoryDataSource {
    suspend fun insert(model: SearchHistoryEntry)

    suspend fun removeEntry(id: Long)

    fun getRecentEntries(): Flow<List<SearchHistoryEntry>>
}
