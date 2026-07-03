package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Game
import kotlinx.coroutines.flow.Flow

interface GameDataSource : DataSource<Game> {
    fun getFavorites(): Flow<List<Game>>

    fun getMostSongsGames(): Flow<List<Game>>

    fun getByIdList(ids: List<Long>): Flow<List<Game>>

    fun searchByName(name: String): Flow<List<Game>>

    suspend fun incrementSheetsPlayed(gameId: Long)

    suspend fun toggleFavorite(gameId: Long)

    suspend fun toggleOffline(gameId: Long)

    fun getHighestId(): Flow<Long>
}
