package com.vgleadsheets.database.source

import com.vgleadsheets.model.history.TagValuePlayCount
import kotlinx.coroutines.flow.Flow

interface TagValuePlayCountDataSource {
    suspend fun incrementPlayCount(tagValueId: Long, mostRecentPlay: Long)

    fun getMostPlays(): Flow<List<TagValuePlayCount>>
}
