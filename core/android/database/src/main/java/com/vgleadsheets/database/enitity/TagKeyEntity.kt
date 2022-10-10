package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue

@Entity(tableName = "tag_key")
data class TagKeyEntity(
    @PrimaryKey val id: Long,
    val name: String
) {
    fun toTagKey(values: List<TagValue>?) = TagKey(id, name, values)
}
