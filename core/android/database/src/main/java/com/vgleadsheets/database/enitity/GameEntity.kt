package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.enitity.GameEntity.Companion.TABLE

@Entity(tableName = TABLE)
data class GameEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val hasVocalSongs: Boolean,
    val photoUrl: String? = null
) {
    companion object {
        const val TABLE = "game"
    }
}
