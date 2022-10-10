package com.vgleadsheets.database.dao

import com.vgleadsheets.model.tag.TagKey
import kotlinx.coroutines.flow.Flow

interface TagKeyDao {

    fun getTagKey(tagKeyId: Long): Flow<TagKey>

    fun getAll(): Flow<List<TagKey>>

    suspend fun insertAll(tagKeyEntities: List<TagKey>)

    suspend fun nukeTable()
}
