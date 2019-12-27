package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.jam.JamEntity
import io.reactivex.Observable

@Dao
interface JamDao {
    @Insert
    fun insert(jams: JamEntity)

    @Query("SELECT * FROM jam ORDER BY name")
    fun getAll(): Observable<List<JamEntity>>

    @Query("SELECT * FROM jam WHERE id = :jamId")
    fun getJam(jamId: Long): Observable<JamEntity>

}
