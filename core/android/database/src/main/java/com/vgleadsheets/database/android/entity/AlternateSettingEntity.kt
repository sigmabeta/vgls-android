package com.vgleadsheets.database.android.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.entity.AlternateSettingEntity.Companion.TABLE

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
