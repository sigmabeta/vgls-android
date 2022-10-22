package com.vgleadsheets.conversion.wrapper

import com.vgleadsheets.conversion.OneToManyWrapper
import com.vgleadsheets.conversion.converter.GameConverter
import com.vgleadsheets.conversion.converter.SongConverter
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.enitity.GameEntity
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.database.mapList
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song

class GameRoomDaoWrapper(
    private val convert: GameConverter,
    private val manyConverter: SongConverter,
    private val roomImpl: GameRoomDao,
    private val relatedRoomImpl: SongRoomDao
) : OneToManyWrapper<GameRoomDao, Game, GameEntity, Song, SongEntity, SongRoomDao, GameConverter, SongConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
) {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
