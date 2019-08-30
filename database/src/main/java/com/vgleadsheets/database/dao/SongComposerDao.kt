package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.joins.SongComposerJoin

@Dao
interface SongComposerDao {
    @Insert
    fun insertAll(songComposerJoins: List<SongComposerJoin>): LongArray

    @Query(
        """ 
            SELECT * FROM composer INNER JOIN song_composer_join 
            ON composer.id=song_composer_join.composerId
            WHERE song_composer_join.songId=:songId
            """
    )
    fun getComposersForSong(songId: Long): List<ComposerEntity>

    @Query("DELETE FROM song_composer_join")
    fun nukeTable()
}
