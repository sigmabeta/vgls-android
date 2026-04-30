package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.TagValuePlayCountEntity.Companion.TABLE

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
