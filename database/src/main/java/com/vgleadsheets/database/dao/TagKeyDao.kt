package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.tag.TagKeyEntity
import io.reactivex.Observable

@Dao
interface TagKeyDao {
    @Query("SELECT * FROM tag_key ORDER BY name")
    fun getAll(): Observable<List<TagKeyEntity>>

    @Insert
    fun insertAll(tagKeyEntities: List<TagKeyEntity>)

    @Query("DELETE FROM tag_key")
    fun nukeTable()
}
