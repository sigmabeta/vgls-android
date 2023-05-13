package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.OneToManyAndroidDataSource
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.android.converter.GameConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.ComposersForSongDao
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.dao.GameDataSource
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song

class GameAndroidDataSource(
    private val convert: GameConverter,
    private val manyConverter: SongConverter,
    private val roomImpl: GameRoomDao,
    private val relatedRoomImpl: SongRoomDao,
    private val composersForSongDao: ComposersForSongDao,
    private val composerConverter: ComposerConverter
) : OneToManyAndroidDataSource<GameRoomDao,
    Game,
    GameEntity,
    Song,
    SongEntity,
    SongRoomDao,
    GameConverter,
    SongConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
),
    GameDataSource {
    override fun getFavorites() = roomImpl
        .getFavorites()
        .mapList { convert.entityToModelWithMany(it, relatedRoomImpl, manyConverter) }

    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModelWithMany(it, relatedRoomImpl, manyConverter) }

    override fun getSongsForGame(gameId: Long, withComposers: Boolean) = relatedRoomImpl
        .getEntitiesForForeign(gameId)
        .mapList {
            if (withComposers) {
                manyConverter.entityToModelWithMany(
                    it,
                    composersForSongDao,
                    composerConverter
                )
            } else {
                manyConverter.entityToModel(it)
            }
        }

    override fun incrementSheetsPlayed(gameId: Long) = roomImpl.incrementSheetsPlayed(gameId)

    override fun toggleFavorite(gameId: Long) = roomImpl.toggleFavorite(gameId)

    override fun toggleOffline(gameId: Long) = roomImpl.toggleOffline(gameId)
}
