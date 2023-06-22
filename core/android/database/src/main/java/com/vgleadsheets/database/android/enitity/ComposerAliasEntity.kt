package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.dao.RoomDao.Companion.COLUMN_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.enitity.ComposerAliasEntity.Companion.TABLE

@Entity(
    tableName = TABLE,
    foreignKeys = [
        ForeignKey(
            entity = ComposerEntity::class,
            parentColumns = arrayOf(COLUMN_PRIMARY_KEY_ID),
            childColumns = arrayOf(ComposerEntity.COLUMN_FOREIGN_KEY),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ComposerAliasEntity(
    val composerId: Long,
    val name: String,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    companion object {
        const val TABLE = "alias_composer"
    }
}
