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
    val sheetsPlayed: Int = 0,
    val isFavorite: Boolean = false,
    val isAvailableOffline: Boolean = false,
) {
    companion object {
        const val TABLE = "game"
    }
}
