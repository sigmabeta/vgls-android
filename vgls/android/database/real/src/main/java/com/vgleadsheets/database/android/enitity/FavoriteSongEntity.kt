package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.FavoriteSongEntity.Companion.TABLE

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
