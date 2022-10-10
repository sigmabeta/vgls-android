package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.enitity.ComposerEntity
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.database.join.SongComposerJoin

@Dao
interface SongComposerRoomDao {
    @Insert
    suspend fun insertAll(songComposerJoins: List<SongComposerJoin>)

    @Query(
        """ 
            SELECT * FROM composer INNER JOIN song_composer_join 
            ON composer.id=song_composer_join.composerId
            WHERE song_composer_join.songId=:songId
            ORDER BY name
            COLLATE NOCASE
            """
    )
    fun getComposersForSongSync(songId: Long): List<ComposerEntity>

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

    @Query("DELETE FROM song_composer_join")
    suspend fun nukeTable()
}
