package com.vgleadsheets.repository

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.model.Game
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GameRepository(
    private val dispatchers: VglsDispatchers,
    private val gameAliasDataSource: GameAliasDataSource,
    private val gameDataSource: GameDataSource,
) {
    fun getAllGames() = gameDataSource
        .getAll()
        .flowOn(dispatchers.disk)

    fun getFavoriteGames() = gameDataSource
        .getFavorites()
        .flowOn(dispatchers.disk)

    fun getGame(gameId: Long) = gameDataSource
        .getOneById(gameId)
        .flowOn(dispatchers.disk)

    fun getGameSync(gameId: Long) = gameDataSource
        .getOneByIdSync(gameId)

    fun searchGamesCombined(searchQuery: String) = combine(
        searchGames(searchQuery),
        searchGameAliases(searchQuery)
    ) { games: List<Game>, gameAliases: List<Game> ->
        games + gameAliases
    }.map { games ->
        games.distinctBy { it.id }
    }.flowOn(dispatchers.disk)

    fun getMostSongsGames() = gameDataSource
        .getMostSongsGames()
        .flowOn(dispatchers.disk)

    @Suppress("MaxLineLength")
    private fun searchGames(searchQuery: String) = gameDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.

    @Suppress("MaxLineLength")
    private fun searchGameAliases(searchQuery: String) = gameAliasDataSource
        .searchByName("%$searchQuery%") // Percent characters allow characters before and after the query to match.
        .map { list ->
            list.mapNotNull { it.game }
        }
}
