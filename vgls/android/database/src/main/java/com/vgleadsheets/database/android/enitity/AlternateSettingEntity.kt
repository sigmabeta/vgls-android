package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.AlternateSettingEntity.Companion.TABLE

@Entity(
    tableName = TABLE
)
data class AlternateSettingEntity(
    @PrimaryKey val id: Long,
    val isAltSelected: Boolean,
) {
    companion object {
        const val TABLE = "alternate_setting"
    }
}
