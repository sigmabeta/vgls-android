package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.OfflineComposerEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class OfflineComposerEntity(
    @PrimaryKey val id: Long,
) {
    companion object {
        const val TABLE = "offline_composer"
    }
}
