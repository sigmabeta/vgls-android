package com.vgleadsheets.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vgleadsheets.model.time.Time

@Suppress("ConstructorParameterNaming")
@Entity(tableName = "time")
data class TimeEntity(
    @PrimaryKey val time_id: Int,
    val time_ms: Long
) {
    fun toTime() = Time(time_id, time_ms)
}
