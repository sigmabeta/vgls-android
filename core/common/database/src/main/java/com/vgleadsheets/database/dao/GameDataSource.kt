package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Game
import kotlinx.coroutines.flow.Flow

interface GameDataSource : DataSource<Game> {
    fun getFavorites(): Flow<List<Game>>

    fun getMostSongsGames(): Flow<List<Game>>

    fun getByIdList(ids: List<Long>): Flow<List<Game>>

    fun searchByName(name: String): Flow<List<Game>>

    fun incrementSheetsPlayed(gameId: Long)

    fun toggleFavorite(gameId: Long)

    fun toggleOffline(gameId: Long)
}
