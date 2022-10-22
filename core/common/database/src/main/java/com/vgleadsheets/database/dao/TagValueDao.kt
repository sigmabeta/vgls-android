package com.vgleadsheets.database.dao

import com.vgleadsheets.model.tag.TagValue
import kotlinx.coroutines.flow.Flow

interface TagValueDao : OneToManyDao<TagValue> {
    fun searchByName(name: String): Flow<List<TagValue>>
}
