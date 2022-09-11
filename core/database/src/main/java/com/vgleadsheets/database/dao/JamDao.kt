package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.vgleadsheets.model.jam.JamEntity
import com.vgleadsheets.model.jam.SongHistoryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JamDao {
    @Insert(onConflict = REPLACE)
    fun insert(jam: JamEntity)

    @Query("SELECT * FROM jam ORDER BY name COLLATE NOCASE")
    fun getAll(): Flow<List<JamEntity>>

    @Query("SELECT * FROM jam WHERE id = :jamId")
    fun getJam(jamId: Long): Flow<JamEntity>

    @Query("DELETE FROM jam WHERE Id = :jamId")
    fun remove(jamId: Long)

    @Transaction
    fun upsertJam(
        songHistoryEntryDao: SongHistoryEntryDao,
        jam: JamEntity,
        songHistoryEntries: List<SongHistoryEntryEntity>
    ) {
        songHistoryEntryDao.removeAllForJam(jam.id)
        insert(jam)
        songHistoryEntryDao.insertAll(songHistoryEntries)
    }

    @Query("DELETE FROM jam")
    fun nukeTable()
}
