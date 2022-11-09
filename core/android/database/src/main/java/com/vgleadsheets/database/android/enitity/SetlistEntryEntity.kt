package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.dao.RoomDao.Companion.ROW_PRIMARY_KEY_ID
import com.vgleadsheets.database.android.enitity.SetlistEntryEntity.Companion.ROW_FOREIGN_KEY
import com.vgleadsheets.database.android.enitity.SetlistEntryEntity.Companion.TABLE

@Suppress("ConstructorParameterNaming")
@Entity(
    tableName = TABLE,
    foreignKeys = [
        ForeignKey(
            entity = JamEntity::class,
            parentColumns = arrayOf(ROW_PRIMARY_KEY_ID),
            childColumns = arrayOf(ROW_FOREIGN_KEY),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SetlistEntryEntity(
    @PrimaryKey val id: Long,
    val game_name: String,
    val song_name: String,
    val jam_id: Long,
    val song_id: Long
) {
    companion object {
        const val TABLE = "setlist_entry"

        const val ROW_FOREIGN_KEY = "jam_id"
    }
}