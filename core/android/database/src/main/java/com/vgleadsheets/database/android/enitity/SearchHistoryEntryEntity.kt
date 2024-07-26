package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.SearchHistoryEntryEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class SearchHistoryEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val query: String,
    val timeMs: Long,
) {
    companion object {
        const val TABLE = "search_history_entry"
    }
}
