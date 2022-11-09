package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.song.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM song ORDER BY name, gameName")
    fun getAll(): Flow<List<SongEntity>>

    @Query("SELECT * FROM song WHERE game_id = :gameId ORDER BY name, gameName")
    fun getSongsForGame(gameId: Long): Flow<List<SongEntity>>

    @Query("SELECT * FROM song WHERE game_id = :gameId ORDER BY name, gameName")
    fun getSongsForGameSync(gameId: Long): List<SongEntity>

    @Query("SELECT * FROM song WHERE id = :songId")
    fun getSong(songId: Long): Flow<SongEntity>

    @Query("SELECT * FROM song WHERE id = :songId")
    fun getSongSync(songId: Long): SongEntity?

    @Query("SELECT * FROM song WHERE name LIKE :title ORDER BY name, gameName")
    fun searchSongsByTitle(title: String): Flow<List<SongEntity>>

    @Insert
    suspend fun insertAll(songs: List<SongEntity>)

    @Query("DELETE FROM song")
    suspend fun nukeTable()
}
