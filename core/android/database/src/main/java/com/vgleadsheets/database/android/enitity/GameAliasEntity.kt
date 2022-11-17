package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.enitity.GameAliasEntity.Companion.ROW_FOREIGN_KEY
import com.vgleadsheets.database.android.enitity.GameAliasEntity.Companion.TABLE

@Entity(
    tableName = TABLE,
    foreignKeys = [
        ForeignKey(
            entity = GameEntity::class,
            parentColumns = arrayOf(ROW_PRIMARY_KEY_ID),
            childColumns = arrayOf(ROW_FOREIGN_KEY),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GameAliasEntity(
    val gameId: Long,
    val name: String,
    val photoUrl: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    companion object {
        const val TABLE = "alias_game"

        const val ROW_FOREIGN_KEY = "gameId"
    }
}
