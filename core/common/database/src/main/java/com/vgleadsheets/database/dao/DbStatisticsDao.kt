package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vgleadsheets.model.time.TimeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DbStatisticsDao {
    @Query("SELECT * FROM time WHERE time_id = :tableId")
    fun getTime(tableId: Int): Flow<List<TimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dbStatisticsEntity: TimeEntity)

    @Query("DELETE FROM time")
    suspend fun nukeTable()
}
