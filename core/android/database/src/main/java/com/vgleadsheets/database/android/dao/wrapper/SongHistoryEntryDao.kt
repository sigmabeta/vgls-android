package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.model.jam.SongHistoryEntry

class SongHistoryEntryRoomDaoWrapper(
    private val roomImpl: RoomDao
) : Dao {

    override fun getSongHistoryEntriesForJamSync(jamId: Long): List<SongHistoryEntry>

    override suspend fun insertAll(songHistoryEntries: List<SongHistoryEntry>): List<Long>

    override suspend fun removeAllForJam(jamId: Long)

    override suspend fun nukeTable()
}
