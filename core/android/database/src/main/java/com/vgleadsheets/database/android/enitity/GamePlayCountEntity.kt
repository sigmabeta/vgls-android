package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.GamePlayCountEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class GamePlayCountEntity(
    @PrimaryKey val id: Long,
    val playCount: Int,
    val mostRecentPlay: Long,
) {
    companion object {
        const val TABLE = "game_play_count"
    }
}
