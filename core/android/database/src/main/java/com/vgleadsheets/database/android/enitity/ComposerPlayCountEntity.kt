package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.ComposerPlayCountEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class ComposerPlayCountEntity(
    @PrimaryKey val id: Long,
    val playCount: Int,
) {
    companion object {
        const val TABLE = "composer_play_count"
    }
}
