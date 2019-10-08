package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.pages.PageEntity
import com.vgleadsheets.model.parts.PartEntity
import com.vgleadsheets.model.song.SongEntity
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface GameDao {
    @Query("SELECT * FROM game WHERE id = :gameId")
    fun getGame(gameId: Long): Observable<GameEntity>

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
        partDao: PartDao,
        pageDao: PageDao,
        songs: List<SongEntity>,
        composerEntities: List<ComposerEntity>,
        parts: List<PartEntity>,
        pages: List<PageEntity>,
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

        pageDao.nukeTable()
        pageDao.insertAll(pages)
    }

    @Query("UPDATE game SET giantBombId = :giantBombId, photoUrl = :photoUrl WHERE id = :vglsId;")
    fun giantBombifyGame(vglsId: Long, giantBombId: Long, photoUrl: String?): Single<Int>
}
