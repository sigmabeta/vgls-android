package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vgleadsheets.model.DbStatisticsEntity
import com.vgleadsheets.model.song.SongEntity
import io.reactivex.Observable

@Dao
interface DbStatisticsDao {
    @Query("SELECT * FROM db_statistics WHERE table_id = :table_id")
    fun getLastEditDate(table_id: Int): Observable<DbStatisticsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dbStatisticsEntity: DbStatisticsEntity)
}