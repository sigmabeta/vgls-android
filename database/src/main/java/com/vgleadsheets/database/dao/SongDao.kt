package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.song.SongEntity
import io.reactivex.Observable

@Dao
interface SongDao {
    @Query("SELECT * FROM song WHERE game_id = :game_id")
    fun getSongsForGame(game_id: Long): Observable<List<SongEntity>>

    @Query("SELECT * FROM song WHERE game_id = :game_id")
    fun getSongsForGameSync(game_id: Long): List<SongEntity>

    @Query("SELECT * FROM song WHERE id = :song_id")
    fun getSong(song_id: Long): Observable<SongEntity>

    @Insert
    fun insertAll(songs: List<SongEntity>)

    @Query("DELETE FROM song")
    fun nukeTable()
}
