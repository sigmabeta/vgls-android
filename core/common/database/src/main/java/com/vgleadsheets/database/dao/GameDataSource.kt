package com.vgleadsheets.database.dao

import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import kotlinx.coroutines.flow.Flow

interface GameDataSource : OneToManyDataSource<Game> {
    fun getFavorites(): Flow<List<Game>>

    fun searchByName(name: String): Flow<List<Game>>

    fun getSongsForGame(gameId: Long, withComposers: Boolean): Flow<List<Song>>

    fun incrementSheetsPlayed(gameId: Long)

    fun toggleFavorite(gameId: Long)

    fun toggleOffline(gameId: Long)
}
