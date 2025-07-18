package com.vgleadsheets.database.android.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.entity.SongHistoryEntryEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class SongHistoryEntryEntity(
    val songId: Long,
    val timeMs: Long,
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
) {
    companion object {
        const val TABLE = "song_history_entry"
    }
}
