package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.jam.SetlistEntryEntity
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface SetlistEntryDao {
    @Query("SELECT * FROM setlist_entry WHERE jam_id = :jamId")
    fun getSetlistEntriesForJam(jamId: Long): Observable<List<SetlistEntryEntity>>

    @Insert
    fun insertAll(setlistEntries: List<SetlistEntryEntity>): Single<List<Long>>

    @Query("DELETE FROM setlist_entry WHERE jam_id = :jamId")
    fun removeAllForJam(jamId: Long)

    @Query("DELETE FROM setlist_entry")
    fun nukeTable()
}
