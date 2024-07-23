package com.vgleadsheets.database.source

import com.vgleadsheets.model.history.SongPlayCount
import kotlinx.coroutines.flow.Flow

interface SongPlayCountDataSource {
    suspend fun incrementPlayCount(songId: Long, mostRecentPlay: Long)

    fun getMostPlays(): Flow<List<SongPlayCount>>
}
