package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.AndroidDataSource
import com.vgleadsheets.conversion.android.converter.GameConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.model.Game

class GameAndroidDataSource(
    private val convert: GameConverter,
    private val roomImpl: GameRoomDao,
) : AndroidDataSource<GameRoomDao, Game, GameEntity, GameConverter>(
    convert,
    roomImpl,
), GameDataSource {
    override fun getFavorites() = roomImpl
        .getFavorites()
        .mapList { convert.entityToModel(it) }

    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }

    override fun incrementSheetsPlayed(gameId: Long) = roomImpl.incrementSheetsPlayed(gameId)

    override fun toggleFavorite(gameId: Long) = roomImpl.toggleFavorite(gameId)

    override fun toggleOffline(gameId: Long) = roomImpl.toggleOffline(gameId)
}
