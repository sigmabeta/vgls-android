package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.enitity.GameAliasEntity.Companion.TABLE

@Entity(
    tableName = TABLE,
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = arrayOf(COLUMN_PRIMARY_KEY_ID),
            childColumns = arrayOf(GameEntity.COLUMN_FOREIGN_KEY_ALIAS),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GameAliasEntity(
    val gameId: Long,
    val name: String,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    companion object {
        const val TABLE = "alias_game"
    }
}
