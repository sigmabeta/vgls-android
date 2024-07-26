package com.vgleadsheets.repository

import com.vgleadsheets.database.dao.GameDataSource

class GameRepository(
    private val gameDataSource: GameDataSource,
) {
    fun getAllGames() = gameDataSource
        .getAll()

    fun getFavoriteGames() = gameDataSource
        .getFavorites()

    fun getGame(gameId: Long) = gameDataSource
        .getOneById(gameId)

    fun getGameSync(gameId: Long) = gameDataSource
        .getOneByIdSync(gameId)

    fun getMostSongsGames() = gameDataSource
        .getMostSongsGames()
}
