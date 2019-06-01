package com.vgleadsheets.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "db_statistics")
data class DbStatisticsEntity (@PrimaryKey val table_id: Long,
                               val last_edit_time_ms: Long)