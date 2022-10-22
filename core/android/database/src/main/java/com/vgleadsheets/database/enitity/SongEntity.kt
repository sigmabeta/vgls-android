package com.vgleadsheets.database.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.vgleadsheets.database.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.enitity.SongEntity.Companion.ROW_FOREIGN_KEY
import com.vgleadsheets.database.enitity.SongEntity.Companion.TABLE

@Suppress("ConstructorParameterNaming")
@Entity(
    tableName = TABLE,
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = arrayOf(ROW_PRIMARY_KEY_ID),
            childColumns = arrayOf(ROW_FOREIGN_KEY),
            onDelete = CASCADE
        )
    ]
)
data class SongEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val filename: String,
    val pageCount: Int,
    val lyricPageCount: Int,
    val gameName: String,
    val game_id: Long,
    val hasVocals: Boolean,
    val playCount: Int = 0
) {
    companion object {
        const val TABLE = "song"

        const val ROW_FOREIGN_KEY = "game_id"
    }
}

