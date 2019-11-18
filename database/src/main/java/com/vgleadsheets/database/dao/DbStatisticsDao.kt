package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vgleadsheets.model.time.TimeEntity
import io.reactivex.Observable

@Dao
interface DbStatisticsDao {
    @Query("SELECT * FROM time WHERE time_id = :tableId")
    fun getTime(tableId: Int): Observable<List<TimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbStatisticsEntity: TimeEntity)
}
