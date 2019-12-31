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
    fun insertAll(songHistoryEntries: List<SongHistoryEntryEntity>): List<Long>

    @Query("DELETE FROM song_history_entry WHERE jam_id = :jamId")
    fun removeAllForJam(jamId: Long)

    // TODO Use this instead if we ever get web-provided DB id's for songHistory entries
    /*@Transaction
    fun replaceSongHistory(jamId: Long, songHistoryEntries: List<SongHistoryEntryEntity>) {
        removeAllForJam(jamId)
        insertAll(songHistoryEntries)
    }*/
}
