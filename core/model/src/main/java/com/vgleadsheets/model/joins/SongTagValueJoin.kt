package com.vgleadsheets.model.joins

import androidx.room.Entity
import androidx.room.ForeignKey
import com.vgleadsheets.model.song.SongEntity
import com.vgleadsheets.model.tag.TagValueEntity

@Entity(
    tableName = "song_tag_value_join",
    primaryKeys = ["songId", "tagValueId"],
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("songId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TagValueEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("tagValueId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SongTagValueJoin(val songId: Long, val tagValueId: Long)
