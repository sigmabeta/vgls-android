package com.vgleadsheets.database.android.dao.wrapper

import com.vgleadsheets.model.time.Time
import kotlinx.coroutines.flow.Flow

class DbStatisticsRoomDaoWrapper(
    private val roomImpl: RoomDao
) : Dao {

    override fun getTime(tableId: Int): Flow<List<Time>>

    override suspend fun insert(dbStatistics: Time)

    override suspend fun nukeTable()
}
