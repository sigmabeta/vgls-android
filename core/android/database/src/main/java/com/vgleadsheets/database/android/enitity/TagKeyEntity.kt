package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.TagKeyEntity.Companion.TABLE

@Entity(tableName = TABLE)
data class TagKeyEntity(
    @PrimaryKey val id: Long,
    val name: String
) {
    companion object {
        const val TABLE = "tag_key"
    }
}
