package com.vgleadsheets.database.dao

import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

interface TagValueDao {

    fun getTagValue(tagValueId: Long): Flow<TagValue>

    fun getValuesForTag(tagKeyId: Long): Flow<List<TagValue>>

    fun getValuesForTagSync(tagKeyId: Long): List<TagValue>

    suspend fun insertAll(tagValues: List<TagValue>)

    suspend fun nukeTable()
}
