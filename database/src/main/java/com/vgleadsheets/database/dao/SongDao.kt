package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.song.SongEntity
import io.reactivex.Observable

@Dao
interface SongDao {
    @Query("SELECT * FROM song ORDER BY name, gameName")
    fun getAll(): Observable<List<SongEntity>>

    @Query("SELECT * FROM song WHERE game_id = :gameId ORDER BY name, gameName")
    fun getSongsForGame(gameId: Long): Observable<List<SongEntity>>

    @Query("SELECT * FROM song WHERE game_id = :gameId ORDER BY name, gameName")
    fun getSongsForGameSync(gameId: Long): List<SongEntity>

    @Query("SELECT * FROM song WHERE id = :songId")
    fun getSong(songId: Long): Observable<SongEntity>

    @Query("SELECT * FROM song WHERE name LIKE :title ORDER BY name, gameName")
    fun searchSongsByTitle(title: String): Observable<List<SongEntity>>

    @Insert
    fun insertAll(songs: List<SongEntity>)

    @Query("DELETE FROM song")
    fun nukeTable()
}
