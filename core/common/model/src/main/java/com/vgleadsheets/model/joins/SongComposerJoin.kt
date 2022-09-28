package com.vgleadsheets.model.joins

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import com.vgleadsheets.database.model.ComposerEntity
import com.vgleadsheets.database.model.SongEntity

@Entity(
    tableName = "song_composer_join",
    primaryKeys = ["songId", "composerId"],
    foreignKeys = [
        ForeignKey(
            entity = com.vgleadsheets.database.model.SongEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("songId"),
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = com.vgleadsheets.database.model.ComposerEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("composerId"),
            onDelete = CASCADE
        )
    ]
)
data class SongComposerJoin(val songId: Long, val composerId: Long)
