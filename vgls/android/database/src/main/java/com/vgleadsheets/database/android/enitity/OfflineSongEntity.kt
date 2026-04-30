package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.OfflineSongEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class OfflineSongEntity(
    @PrimaryKey val id: Long,
) {
    companion object {
        const val TABLE = "offline_song"
    }
}
