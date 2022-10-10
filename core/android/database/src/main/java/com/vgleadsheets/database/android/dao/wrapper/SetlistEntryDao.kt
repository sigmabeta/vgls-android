package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.model.jam.SetlistEntry
import kotlinx.coroutines.flow.Flow

class SetlistEntryRoomDaoWrapper(
    private val roomImpl: RoomDao
) : Dao {

    override fun getSetlistEntriesForJam(jamId: Long): Flow<List<SetlistEntry>>

    override suspend fun insertAll(setlistEntries: List<SetlistEntry>): List<Long>

    override suspend fun removeAllForJam(jamId: Long)

    override suspend fun nukeTable()
}
