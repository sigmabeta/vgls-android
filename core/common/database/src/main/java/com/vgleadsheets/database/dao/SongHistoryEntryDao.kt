package com.vgleadsheets.database.dao

import com.vgleadsheets.model.jam.SongHistoryEntry

interface SongHistoryEntryDao {

    fun getSongHistoryEntriesForJamSync(jamId: Long): List<SongHistoryEntry>

    suspend fun insertAll(songHistoryEntries: List<SongHistoryEntry>): List<Long>

    suspend fun removeAllForJam(jamId: Long)

    suspend fun nukeTable()
}
