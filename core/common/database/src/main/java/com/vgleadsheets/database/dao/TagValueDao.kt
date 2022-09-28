package com.vgleadsheets.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vgleadsheets.model.tag.TagValueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TagValueDao {
    @Query("SELECT * FROM tag_value WHERE id = :tagValueId")
    fun getTagValue(tagValueId: Long): Flow<TagValueEntity>

    @Query("SELECT * FROM tag_value WHERE tag_key_id = :tagKeyId ORDER BY name COLLATE NOCASE")
    fun getValuesForTag(tagKeyId: Long): Flow<List<TagValueEntity>>

    @Query("SELECT * FROM tag_value WHERE tag_key_id = :tagKeyId ORDER BY name COLLATE NOCASE")
    fun getValuesForTagSync(tagKeyId: Long): List<TagValueEntity>

    @Insert
    suspend fun insertAll(tagValues: List<TagValueEntity>)

    @Query("DELETE FROM tag_value")
    suspend fun nukeTable()
}
