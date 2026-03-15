package com.vgleadsheets.conversion.android.datasource

import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.DbStatisticsRoomDao
import com.vgleadsheets.database.android.dao.OfflineUpdateRoomDao
import com.vgleadsheets.database.android.dao.OfflineUpdateRoomDao.Companion.QUERY_ALL
import com.vgleadsheets.database.android.enitity.OfflineUpdateResultEntity
import com.vgleadsheets.database.android.enitity.TimeEntity
import com.vgleadsheets.database.dao.OfflineUpdateDataSource
import com.vgleadsheets.model.time.Time
import com.vgleadsheets.model.updates.OfflineUpdateResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OfflineUpdateAndroidDataSource(
    private val roomImpl: OfflineUpdateRoomDao
) : OfflineUpdateDataSource {
    override suspend fun insert(entity: OfflineUpdateResult) = 

    override fun getAll(): Flow<List<OfflineUpdateResult>

    override fun nukeTable() = roomImpl.nukeTable()
}
