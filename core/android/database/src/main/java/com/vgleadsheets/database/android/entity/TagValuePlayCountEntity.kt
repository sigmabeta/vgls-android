package com.vgleadsheets.database.android.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.entity.TagValuePlayCountEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class TagValuePlayCountEntity(
    @PrimaryKey val id: Long,
    val playCount: Int,
    val mostRecentPlay: Long,
) {
    companion object {
        const val TABLE = "tag_value_play_count"
    }
}
