package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.enitity.SongAliasEntity.Companion.ROW_FOREIGN_KEY
import com.vgleadsheets.database.android.enitity.SongAliasEntity.Companion.TABLE

@Entity(
    tableName = TABLE,
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = arrayOf(ROW_PRIMARY_KEY_ID),
            childColumns = arrayOf(ROW_FOREIGN_KEY),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SongAliasEntity(
    val songId: Long,
    val name: String,
    @PrimaryKey(autoGenerate = true) val id: Long? = null
) {
    companion object {
        const val TABLE = "alias_song"

        const val ROW_FOREIGN_KEY = "songId"
    }
}
