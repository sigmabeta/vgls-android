package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.SongHistoryEntry
import kotlinx.coroutines.flow.Flow

class JamRoomDaoWrapper(
    private val roomImpl: RoomDao
): Dao {
    override suspend fun insert(jam: Jam)

    override fun getAll(): Flow<List<Jam>>

    override fun getJam(jamId: Long): Flow<Jam>

    override suspend fun remove(jamId: Long)

    override suspend fun upsertJam(
        songHistoryEntryDao: SongHistoryEntryDao,
        jam: Jam,
        songHistoryEntries: List<SongHistoryEntry>
    )

    override suspend fun nukeTable()
}
