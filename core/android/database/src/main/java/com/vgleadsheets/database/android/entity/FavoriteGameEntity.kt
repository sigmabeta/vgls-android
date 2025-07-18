package com.vgleadsheets.database.android.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.entity.FavoriteGameEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class FavoriteGameEntity(
    @PrimaryKey val id: Long,
) {
    companion object {
        const val TABLE = "favorite_game"
    }
}
