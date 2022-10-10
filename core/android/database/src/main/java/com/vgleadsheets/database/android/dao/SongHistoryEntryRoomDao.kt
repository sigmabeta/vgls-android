package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.enitity.SongHistoryEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongHistoryEntryRoomDao : RoomDao<SongHistoryEntryEntity> {
    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<SongHistoryEntryEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): SongHistoryEntryEntity

    @Query("SELECT * FROM song_history_entry ORDER BY name COLLATE NOCASE")
    override fun getAll(): Flow<List<SongHistoryEntryEntity>>

    @Insert
    override suspend fun insert(models: List<SongHistoryEntryEntity>)

    @Query("DELETE FROM song_history_entry")
    override suspend fun nukeTable()

    companion object {
        const val QUERY_SINGLE = "SELECT * FROM song_history_entry WHERE id = :id"
    }
}

