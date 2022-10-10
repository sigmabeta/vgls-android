package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.vgleadsheets.database.dao.SongHistoryEntryDao
import com.vgleadsheets.database.enitity.JamEntity
import com.vgleadsheets.database.enitity.SongHistoryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JamRoomDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(jam: JamEntity)

    @Query("SELECT * FROM jam ORDER BY name COLLATE NOCASE")
    fun getAll(): Flow<List<JamEntity>>

    @Query("SELECT * FROM jam WHERE id = :jamId")
    fun getJam(jamId: Long): Flow<JamEntity>

    @Query("DELETE FROM jam WHERE Id = :jamId")
    suspend fun remove(jamId: Long)

    @Transaction
    suspend fun upsertJam(
        songHistoryEntryDao: SongHistoryEntryDao,
        jam: JamEntity,
        songHistoryEntries: List<SongHistoryEntryEntity>
    ) {
        songHistoryEntryDao.removeAllForJam(jam.id)
        insert(jam)

        if (songHistoryEntries.isNotEmpty()) {
            songHistoryEntryDao.insertAll(songHistoryEntries)
        }
    }

    @Query("DELETE FROM jam")
    suspend fun nukeTable()
}
