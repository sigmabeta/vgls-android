package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.database.android.dao.DbStatisticsRoomDao
import com.vgleadsheets.database.android.enitity.TimeEntity
import com.vgleadsheets.database.dao.DbStatisticsDataSource
import com.vgleadsheets.model.time.Time
import javax.inject.Inject
import kotlinx.coroutines.flow.map

class DbStatisticsAndroidDataSource(
    private val roomImpl: DbStatisticsRoomDao
) : DbStatisticsDataSource {
    override fun getTime(tableId: Int) = roomImpl
        .getTime(tableId)
        .map { entity -> entity?.toTime() ?: Time(-1, 0L) }

    override fun insert(dbStatistics: Time) = roomImpl
        .insert(
            TimeEntity(dbStatistics.id, dbStatistics.timeMs)
        )

    override suspend fun nukeTable() = roomImpl.nukeTable()
}
