package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.enitity.SongEntity.Companion.TABLE

@Suppress("ConstructorParameterNaming")
@Entity(
    tableName = TABLE,
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = arrayOf(COLUMN_PRIMARY_KEY_ID),
            childColumns = arrayOf(GameEntity.COLUMN_FOREIGN_KEY),
            onDelete = CASCADE
        )
    ]
)
data class SongEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val filename: String,
    val pageCount: Int,
    val altPageCount: Int,
    val lyricPageCount: Int,
    val gameName: String,
    val game_id: Long,
    val hasVocals: Boolean,
    val playCount: Int,
    val isFavorite: Boolean,
    val isAvailableOffline: Boolean,
    val isAltSelected: Boolean,
) {
    companion object {
        const val TABLE = "song"
    }
}
