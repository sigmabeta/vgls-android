package com.vgleadsheets.database.android.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.entity.ComposerEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class ComposerEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val songCount: Int,
    val hasVocalSongs: Boolean = false,
    val photoUrl: String? = null,
    val sheetsPlayed: Int,
    val isFavorite: Boolean,
    val isAvailableOffline: Boolean,
) {
    companion object {
        const val TABLE = "composer"

        const val COLUMN_FOREIGN_KEY = "composerId"
    }
}
