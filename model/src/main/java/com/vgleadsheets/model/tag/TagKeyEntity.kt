package com.vgleadsheets.model.tag

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag_key")
data class TagKeyEntity(
    @PrimaryKey val id: Long,
    val name: String
)
