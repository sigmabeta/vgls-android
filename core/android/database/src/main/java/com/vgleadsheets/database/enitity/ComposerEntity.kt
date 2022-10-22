package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.enitity.ComposerEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class ComposerEntity(
    @PrimaryKey val id: Long,
    val name: String,
    var hasVocalSongs: Boolean = false,
    val photoUrl: String? = null
) {
    companion object {
        const val TABLE = "composer"
    }
}
