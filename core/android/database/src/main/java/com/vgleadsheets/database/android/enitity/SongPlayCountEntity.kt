package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.SongPlayCountEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class SongPlayCountEntity(
    @PrimaryKey val id: Long,
    val playCount: Int,
    val mostRecentPlay: Long,
) {
    companion object {
        const val TABLE = "song_play_count"
    }
}
