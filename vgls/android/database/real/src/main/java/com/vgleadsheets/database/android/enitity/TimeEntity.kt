package com.vgleadsheets.database.android.enitity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.database.android.enitity.TimeEntity.Companion.TABLE
import com.vgleadsheets.model.time.Time

@Suppress("ConstructorParameterNaming")
@Entity(tableName = TABLE)
data class TimeEntity(
    @PrimaryKey val time_id: Int,
    val time_ms: Long
) {
    fun toTime() = Time(time_id, time_ms)

    companion object {
        const val TABLE = "time"
    }
}
