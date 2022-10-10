package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.enitity.GameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRoomDao : RoomDao<GameEntity> {
    @Query("SELECT * FROM game WHERE name LIKE :name ORDER BY name COLLATE NOCASE")
    fun searchByName(name: String): Flow<List<GameEntity>>

    @Query(QUERY_SINGLE)
    override fun getOneById(id: Long): Flow<GameEntity>

    @Query(QUERY_SINGLE)
    override fun getOneByIdSync(id: Long): GameEntity

    @Query("SELECT * FROM game ORDER BY name COLLATE NOCASE")
    override fun getAll(): Flow<List<GameEntity>>

    @Insert
    override suspend fun insert(models: List<GameEntity>)

    @Query("DELETE FROM game")
    override suspend fun nukeTable()

    companion object {
        const val QUERY_SINGLE = "SELECT * FROM game WHERE id = :id"
    }
}
