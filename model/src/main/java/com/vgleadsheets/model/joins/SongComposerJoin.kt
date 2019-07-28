package com.vgleadsheets.model.joins

import androidx.room.Entity
import androidx.room.ForeignKey
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.song.Song

@Entity(
    tableName = "song_composer_join",
    primaryKeys = ["songId", "composerId"],
    foreignKeys = [
        ForeignKey(
            entity = Song::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("songId")
        ),
        ForeignKey(
            entity = Composer::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("composerId")
        )
    ]
)
class SongComposerJoin(val songId: Int, val composerId: Int)