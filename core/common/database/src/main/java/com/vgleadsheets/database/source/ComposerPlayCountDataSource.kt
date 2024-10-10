package com.vgleadsheets.database.source

import com.vgleadsheets.model.history.ComposerPlayCount
import kotlinx.coroutines.flow.Flow

interface ComposerPlayCountDataSource {
    suspend fun incrementPlayCount(composerId: Long, mostRecentPlay: Long)

    fun getMostPlays(): Flow<List<ComposerPlayCount>>
}
