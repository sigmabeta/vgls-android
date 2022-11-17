package com.vgleadsheets.database.dao

import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

interface TagKeyDataSource : OneToManyDataSource<TagKey> {
    fun getTagValuesForTagKey(tagKeyId: Long): Flow<List<TagValue>>
}
