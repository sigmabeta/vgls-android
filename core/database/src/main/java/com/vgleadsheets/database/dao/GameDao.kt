package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.game.GameEntity
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.joins.SongTagValueJoin
import com.vgleadsheets.model.song.SongEntity
import com.vgleadsheets.model.tag.TagKeyEntity
import com.vgleadsheets.model.tag.TagValueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM game WHERE id = :gameId")
    fun getGame(gameId: Long): Flow<GameEntity>

    @Query("SELECT * FROM game ORDER BY name COLLATE NOCASE")
    fun getAll(): Flow<List<GameEntity>>

    @Query("SELECT * FROM game WHERE name LIKE :title ORDER BY name COLLATE NOCASE")
    fun searchGamesByTitle(title: String): Flow<List<GameEntity>>

    @Insert
    fun insertAll(gameEntities: List<GameEntity>)

    @Query("DELETE FROM game")
    fun nukeTable()

        @Transaction
    fun refreshTable(
        gameEntities: List<GameEntity>,
        songDao: SongDao,
        composerDao: ComposerDao,
        songComposerDao: SongComposerDao,
        songTagValueDao: SongTagValueDao,
        tagKeyDao: TagKeyDao,
        tagValueDao: TagValueDao,
        songs: List<SongEntity>,
        composerEntities: List<ComposerEntity>,
        songComposerJoins: List<SongComposerJoin>,
        songTagValueJoins: List<SongTagValueJoin>,
        tagKeys: List<TagKeyEntity>,
        tagValues: List<TagValueEntity>
    ) {
        nukeTable()
        insertAll(gameEntities)

        songDao.nukeTable()
        songDao.insertAll(songs)

        composerDao.nukeTable()
        composerDao.insertAll(composerEntities)

        songComposerDao.nukeTable()
        songComposerDao.insertAll(songComposerJoins)

        tagKeyDao.nukeTable()
        tagKeyDao.insertAll(tagKeys)

        tagValueDao.nukeTable()
        tagValueDao.insertAll(tagValues)

        songTagValueDao.nukeTable()
        songTagValueDao.insertAll(songTagValueJoins)
    }
}
