package com.vgleadsheets.database.android.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vgleadsheets.database.enitity.TimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DbStatisticsRoomDao {
    @Query("SELECT * FROM time WHERE time_id = :tableId")
    fun getTime(tableId: Int): Flow<TimeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbStatisticsEntity: TimeEntity)

    @Query("DELETE FROM time")
    suspend fun nukeTable()
}
