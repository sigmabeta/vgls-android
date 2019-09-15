package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.vgleadsheets.database.TableName
import com.vgleadsheets.model.DbStatisticsEntity
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.parts.PartEntity
import com.vgleadsheets.model.song.SongEntity
import io.reactivex.Observable

@Dao
interface GameDao {
    @Query("SELECT * FROM game ORDER BY name")
    fun getAll(): Observable<List<GameEntity>>

    @Query("SELECT * FROM game WHERE name LIKE :title")
    fun searchGamesByTitle(title: String): Observable<List<GameEntity>>

    @Insert
    fun insertAll(gameEntities: List<GameEntity>)

    @Query("DELETE FROM game")
    fun nukeTable()

    @Suppress("LongParameterList")
    @Transaction
    fun refreshTable(
        gameEntities: List<GameEntity>,
        songDao: SongDao,
        composerDao: ComposerDao,
        songComposerDao: SongComposerDao,
        dbStatisticsDao: DbStatisticsDao,
        partDao: PartDao,
        songs: List<SongEntity>,
        composerEntities: List<ComposerEntity>,
        parts: List<PartEntity>,
        songComposerJoins: List<SongComposerJoin>
    ) {
        nukeTable()
        insertAll(gameEntities)

        songDao.nukeTable()
        songDao.insertAll(songs)

        composerDao.nukeTable()
        composerDao.insertAll(composerEntities)

        songComposerDao.nukeTable()
        songComposerDao.insertAll(songComposerJoins)

        partDao.nukeTable()
        partDao.insertAll(parts)

        val now = System.currentTimeMillis()
        dbStatisticsDao.insert(DbStatisticsEntity(TableName.GAME.ordinal.toLong(), now))
        dbStatisticsDao.insert(DbStatisticsEntity(TableName.SONG.ordinal.toLong(), now))
    }
}
