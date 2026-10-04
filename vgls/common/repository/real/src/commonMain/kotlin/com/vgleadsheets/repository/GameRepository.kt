package com.vgleadsheets.repository

import com.vgleadsheets.database.dao.GameDataSource

class GameRepository(
    private val gameDataSource: GameDataSource,
) {
    fun getAllGames() = gameDataSource
        .getAll()

    suspend fun getGamesPage(limit: Int, offset: Int) = gameDataSource
        .getPage(limit, offset)

    fun getFavoriteGames() = gameDataSource
        .getFavorites()

    fun getGame(gameId: Long) = gameDataSource
        .getOneById(gameId)

    suspend fun getGameSync(gameId: Long) = gameDataSource
        .getOneByIdSync(gameId)

    fun getMostSongsGames() = gameDataSource
        .getMostSongsGames()
}
