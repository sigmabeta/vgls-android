package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.game.Game
import io.reactivex.Observable

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): Observable<List<Game>>

    @Insert
    fun insertAll(games: List<Game>): LongArray

    @Query("DELETE FROM game")
    fun nukeTable()
}
