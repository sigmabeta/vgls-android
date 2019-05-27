package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.song.Song
import io.reactivex.Observable

@Dao
interface SongDao {
    @Query("SELECT * FROM song WHERE game_id = :game_id")
    fun getSongsForGame(game_id: Int): Observable<List<Song>>

    @Insert
    fun insertAll(vararg songs: Song)
}
