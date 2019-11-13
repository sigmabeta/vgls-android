package com.vgleadsheets.model.tag

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

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
    val tag_key_id: Long
)
