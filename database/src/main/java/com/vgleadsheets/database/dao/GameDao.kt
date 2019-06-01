package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.vgleadsheets.database.TableName
import com.vgleadsheets.model.DbStatisticsEntity
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.song.ApiSong
import com.vgleadsheets.model.song.SongEntity
import io.reactivex.Observable

@Dao
interface GameDao {
    @Query("SELECT * FROM game ORDER BY name")
    fun getAll(): Observable<List<GameEntity>>

    @Insert
    fun insertAll(gameEntities: List<GameEntity>): LongArray

    @Query("DELETE FROM game")
    fun nukeTable()

    @Transaction
    fun refreshTable(
        gameEntities: List<GameEntity>,
        songDao: SongDao,
        dbStatisticsDao: DbStatisticsDao,
        songs: List<SongEntity>
    ) {
        nukeTable()
        insertAll(gameEntities)

        songDao.nukeTable()
        songDao.insertAll(songs)

        val now = System.currentTimeMillis()
        dbStatisticsDao.insert(DbStatisticsEntity(TableName.GAME.ordinal.toLong(), now))
        dbStatisticsDao.insert(DbStatisticsEntity(TableName.SONG.ordinal.toLong(), now))
    }
}
