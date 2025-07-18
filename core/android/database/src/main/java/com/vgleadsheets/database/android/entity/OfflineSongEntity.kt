package com.vgleadsheets.database.android.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.entity.OfflineSongEntity.Companion.TABLE

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
