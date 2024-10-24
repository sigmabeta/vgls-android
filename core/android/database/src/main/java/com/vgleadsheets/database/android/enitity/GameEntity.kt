package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.GameEntity.Companion.TABLE

@Entity(tableName = TABLE)
data class GameEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val hasVocalSongs: Boolean,
    val photoUrl: String? = null,
    val songCount: Int,
    val sheetsPlayed: Int,
    val isFavorite: Boolean,
    val isAvailableOffline: Boolean,
) {
    companion object {
        const val TABLE = "game"

        const val COLUMN_FOREIGN_KEY = "game_id"

        // GameAliases do not use a matching column name. oops.
        const val COLUMN_FOREIGN_KEY_ALIAS = "gameId"
    }
}
