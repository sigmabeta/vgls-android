package com.vgleadsheets.model.joins

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import com.vgleadsheets.model.composer.ComposerEntity
import com.vgleadsheets.model.song.SongEntity

@Entity(
    tableName = "song_composer_join",
    primaryKeys = ["songId", "composerId"],
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("songId"),
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = ComposerEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("composerId"),
            onDelete = CASCADE
        )
    ]
)
data class SongComposerJoin(val songId: Long, val composerId: Long)
