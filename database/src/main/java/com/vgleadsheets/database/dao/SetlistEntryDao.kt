package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vgleadsheets.model.jam.SetlistEntryEntity
import io.reactivex.Observable

@Dao
interface SetlistEntryDao {
    @Query("SELECT * FROM setlist_entry WHERE jam_id = :jamId")
    fun getSetlistEntriesForJam(jamId: Long): Observable<List<SetlistEntryEntity>>

    @Query("SELECT * FROM setlist_entry WHERE jam_id = :jamId")
    fun getSetlistEntriesForJamSync(jamId: Long): List<SetlistEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(setlistEntries: List<SetlistEntryEntity>)

    @Query("DELETE FROM setlist_entry")
    fun nukeTable()
}