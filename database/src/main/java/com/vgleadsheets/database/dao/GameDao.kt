package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.game.GameEntity
import io.reactivex.Observable

@Dao
interface GameDao {
    @Query("SELECT * FROM game")
    fun getAll(): Observable<List<GameEntity>>

    @Insert
    fun insertAll(gameEntities: List<GameEntity>): LongArray

    @Query("DELETE FROM game")
    fun nukeTable()
}
