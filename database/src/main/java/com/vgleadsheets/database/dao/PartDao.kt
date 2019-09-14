package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.parts.PartEntity

@Dao
interface PartDao {
    @Query("SELECT * FROM part WHERE songId = :songId")
    fun getPartsForSongId(songId: Long): List<PartEntity>

    @Insert
    fun insertAll(partEntities: List<PartEntity>)

    @Query("DELETE FROM part")
    fun nukeTable()
}
