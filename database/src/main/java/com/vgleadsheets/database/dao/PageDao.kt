package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.pages.PageEntity

@Dao
interface PageDao {
    @Query("SELECT * FROM page WHERE partId = :partId")
    fun getPagesForPartId(partId: Long): List<PageEntity>

    @Insert
    fun insertAll(pageEntities: List<PageEntity>)

    @Query("DELETE FROM page")
    fun nukeTable()
}
