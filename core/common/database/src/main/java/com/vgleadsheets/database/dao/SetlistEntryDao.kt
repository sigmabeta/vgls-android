package com.vgleadsheets.database.dao

import com.vgleadsheets.model.jam.SetlistEntry
import kotlinx.coroutines.flow.Flow

interface SetlistEntryDao {

    fun getSetlistEntriesForJam(jamId: Long): Flow<List<SetlistEntry>>

    suspend fun insertAll(setlistEntries: List<SetlistEntry>): List<Long>

    suspend fun removeAllForJam(jamId: Long)

    suspend fun nukeTable()
}
