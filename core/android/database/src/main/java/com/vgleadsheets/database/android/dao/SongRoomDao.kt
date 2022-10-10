package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.enitity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongRoomDao : RoomDao<SongEntity> {
    @Query("SELECT * FROM song WHERE name LIKE :name ORDER BY name COLLATE NOCASE")
    fun searchByName(name: String): Flow<List<SongEntity>>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<SongEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): SongEntity

    @Query("SELECT * FROM song ORDER BY name COLLATE NOCASE")
    override fun getAll(): Flow<List<SongEntity>>

    @Insert
    override suspend fun insert(models: List<SongEntity>)

    @Query("DELETE FROM song")
    override suspend fun nukeTable()

    companion object {
        const val QUERY_SINGLE = "SELECT * FROM song WHERE id = :id"
    }
}
