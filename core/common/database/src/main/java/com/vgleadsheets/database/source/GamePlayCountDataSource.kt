package com.vgleadsheets.database.source

import com.vgleadsheets.model.history.GamePlayCount
import kotlinx.coroutines.flow.Flow

interface GamePlayCountDataSource {
    suspend fun incrementPlayCount(gameId: Long, mostRecentPlay: Long)

    fun getAll(): Flow<List<GamePlayCount>>
}
