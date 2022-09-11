package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.joins.SongComposerJoin
import com.vgleadsheets.model.song.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongComposerDao {
    @Insert
    fun insertAll(songComposerJoins: List<SongComposerJoin>)

    @Query(
        """ 
            SELECT * FROM composer INNER JOIN song_composer_join 
            ON composer.id=song_composer_join.composerId
            WHERE song_composer_join.songId=:songId
            ORDER BY name
            COLLATE NOCASE
            """
    )
    fun getComposersForSong(songId: Long): List<ComposerEntity>

    @Query(
        """ 
            SELECT * FROM song INNER JOIN song_composer_join 
            ON song.id=song_composer_join.songId
            WHERE song_composer_join.composerId=:composerId
            ORDER BY name, gameName
            COLLATE NOCASE
            """
    )
    fun getSongsForComposerSync(composerId: Long): List<SongEntity>

    @Query(
        """ 
            SELECT * FROM song INNER JOIN song_composer_join 
            ON song.id=song_composer_join.songId
            WHERE song_composer_join.composerId=:composerId
            ORDER BY name, gameName
            COLLATE NOCASE
            """
    )
    fun getSongsForComposer(composerId: Long): Flow<List<SongEntity>>

    @Query("DELETE FROM song_composer_join")
    fun nukeTable()
}
