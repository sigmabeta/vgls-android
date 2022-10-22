package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.enitity.TagKeyEntity.Companion.TABLE

@Entity(tableName = TABLE)
data class TagKeyEntity(
    @PrimaryKey val id: Long,
    val name: String
) {
    companion object {
        const val TABLE = "tag_key"
    }
}
