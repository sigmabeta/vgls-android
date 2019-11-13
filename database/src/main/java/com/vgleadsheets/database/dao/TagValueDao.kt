package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.tag.TagValueEntity
import io.reactivex.Observable

@Dao
interface TagValueDao {
    @Query("SELECT * FROM tag_value WHERE tag_key_id = :tagKeyId ORDER BY name")
    fun getValuesForTag(tagKeyId: Long): Observable<List<TagValueEntity>>

    @Insert
    fun insertAll(tagValues: List<TagValueEntity>)

    @Query("DELETE FROM tag_value")
    fun nukeTable()
}
