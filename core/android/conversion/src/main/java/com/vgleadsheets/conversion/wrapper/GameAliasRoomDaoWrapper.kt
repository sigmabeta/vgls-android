package com.vgleadsheets.conversion.wrapper

import com.vgleadsheets.conversion.RegularWrapper
import com.vgleadsheets.conversion.converter.GameAliasConverter
import com.vgleadsheets.database.android.dao.GameAliasRoomDao
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.enitity.GameAliasEntity
import com.vgleadsheets.database.mapList
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.GameAlias

class GameAliasRoomDaoWrapper(
    private val convert: GameAliasConverter,
    private val roomImpl: GameAliasRoomDao,
    private val relatedRoomImpl: GameRoomDao
) : RegularWrapper<GameAliasRoomDao, GameAlias, GameAliasEntity, Song, GameRoomDao, GameAliasConverter>(
    convert,
    roomImpl,
    relatedRoomImpl
) {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
