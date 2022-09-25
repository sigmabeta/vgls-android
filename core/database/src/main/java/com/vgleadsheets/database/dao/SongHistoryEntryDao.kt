package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.jam.SongHistoryEntryEntity

@Dao
interface SongHistoryEntryDao {
    @Query("SELECT * FROM song_history_entry WHERE jam_id = :jamId")
    fun getSongHistoryEntriesForJamSync(jamId: Long): List<SongHistoryEntryEntity>

    @Insert
    suspend fun insertAll(songHistoryEntries: List<SongHistoryEntryEntity>): List<Long>

    @Query("DELETE FROM song_history_entry WHERE jam_id = :jamId")
    suspend fun removeAllForJam(jamId: Long)

    @Query("DELETE FROM song_history_entry")
    suspend fun nukeTable()
}
