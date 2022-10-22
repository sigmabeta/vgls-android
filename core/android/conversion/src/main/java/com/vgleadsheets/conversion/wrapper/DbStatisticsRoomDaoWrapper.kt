package com.vgleadsheets.conversion.wrapper

import com.vgleadsheets.database.android.dao.DbStatisticsRoomDao
import com.vgleadsheets.database.dao.DbStatisticsDao
import com.vgleadsheets.database.enitity.TimeEntity
import com.vgleadsheets.model.time.Time
import kotlinx.coroutines.flow.map

class DbStatisticsRoomDaoWrapper(
    private val roomImpl: DbStatisticsRoomDao
) : DbStatisticsDao {
    override fun getTime(tableId: Int) = roomImpl
        .getTime(tableId)
        .map { entity -> entity.toTime() }

    override suspend fun insert(dbStatistics: Time) = roomImpl
        .insert(
            TimeEntity(dbStatistics.id, dbStatistics.timeMs)
        )

    override suspend fun nukeTable() = roomImpl.nukeTable()
}
