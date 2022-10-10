package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.SongHistoryEntry
import kotlinx.coroutines.flow.Flow

interface JamDao {
    suspend fun insert(jam: Jam)

    fun getAll(): Flow<List<Jam>>

    fun getJam(jamId: Long): Flow<Jam>

    suspend fun remove(jamId: Long)

    suspend fun upsertJam(
        songHistoryEntryDao: SongHistoryEntryDao,
        jam: Jam,
        songHistoryEntries: List<SongHistoryEntry>
    )

    suspend fun nukeTable()
}
