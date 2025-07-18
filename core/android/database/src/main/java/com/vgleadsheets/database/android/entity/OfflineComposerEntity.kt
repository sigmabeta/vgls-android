package com.vgleadsheets.database.android.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.entity.OfflineComposerEntity.Companion.TABLE

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
