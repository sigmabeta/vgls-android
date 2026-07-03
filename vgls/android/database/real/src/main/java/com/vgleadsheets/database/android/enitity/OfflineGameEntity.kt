package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.OfflineGameEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class OfflineGameEntity(
    @PrimaryKey val id: Long,
) {
    companion object {
        const val TABLE = "offline_game"
    }
}
