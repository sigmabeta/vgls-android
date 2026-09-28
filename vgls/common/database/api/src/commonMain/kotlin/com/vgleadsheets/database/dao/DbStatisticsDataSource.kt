package com.vgleadsheets.database.dao

import com.vgleadsheets.model.time.Time
import kotlinx.coroutines.flow.Flow

interface DbStatisticsDataSource {

    fun getTime(tableId: Int): Flow<Time>

    suspend fun insert(dbStatistics: Time)

    suspend fun nukeTable()
}
