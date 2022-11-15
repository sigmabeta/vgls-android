package com.vgleadsheets.database.dao

import com.vgleadsheets.model.SongHistoryEntry
import kotlinx.coroutines.flow.Flow

interface SongHistoryEntryDataSource : RegularDataSource<SongHistoryEntry> {
    fun removeForJam(id: Long)

    fun getSongHistoryEntriesForJam(jamId: Long): Flow<List<SongHistoryEntry>>
}
