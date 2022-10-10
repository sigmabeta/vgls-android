package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.enitity.SetlistEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SetlistEntryRoomDao {
    @Query("SELECT * FROM setlist_entry WHERE jam_id = :jamId")
    fun getSetlistEntriesForJam(jamId: Long): Flow<List<SetlistEntryEntity>>

    @Insert
    suspend fun insertAll(setlistEntries: List<SetlistEntryEntity>): List<Long>

    @Query("DELETE FROM setlist_entry WHERE jam_id = :jamId")
    suspend fun removeAllForJam(jamId: Long)

    @Query("DELETE FROM setlist_entry")
    suspend fun nukeTable()
}
