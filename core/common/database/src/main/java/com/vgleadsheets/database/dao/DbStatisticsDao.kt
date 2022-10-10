package com.vgleadsheets.database.dao

import com.vgleadsheets.model.time.Time
import kotlinx.coroutines.flow.Flow

interface DbStatisticsDao {

    fun getTime(tableId: Int): Flow<List<Time>>

    suspend fun insert(dbStatistics: Time)

    suspend fun nukeTable()
}
