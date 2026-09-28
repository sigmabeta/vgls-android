package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.database.android.dao.RoomDao.Companion.GET
import com.vgleadsheets.database.android.dao.RoomDao.Companion.WHERE_SINGLE
import com.vgleadsheets.database.android.enitity.OfflineUpdateResultEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface OfflineUpdateRoomDao {
    @Insert
    suspend fun insert(entity: OfflineUpdateResultEntity)

    @Query(QUERY_ALL)
    fun getAll(): Flow<List<OfflineUpdateResultEntity>>

    @Query("DELETE FROM $TABLE")
    suspend fun nukeTable()

    companion object {
        private const val TABLE = OfflineUpdateResultEntity.TABLE

        const val QUERY_SINGLE = "$GET $TABLE $WHERE_SINGLE"
        const val QUERY_ALL = "$GET $TABLE"
    }
}
