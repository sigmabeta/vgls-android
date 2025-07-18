package com.vgleadsheets.database.android.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.entity.FavoriteSongEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class FavoriteSongEntity(
    @PrimaryKey val id: Long,
) {
    companion object {
        const val TABLE = "favorite_song"
    }
}
