package com.vgleadsheets.model.tag

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.model.song.Song

@Suppress("ConstructorParameterNaming")
@Entity(
    tableName = "tag_value",
    foreignKeys = [
        ForeignKey(
            entity = TagKeyEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("tag_key_id"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class TagValueEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val tag_key_id: Long,
    val tag_key_name: String
) {
    fun toTagValue(songs: List<Song>?) = TagValue(id, name, tag_key_name, songs)
}
