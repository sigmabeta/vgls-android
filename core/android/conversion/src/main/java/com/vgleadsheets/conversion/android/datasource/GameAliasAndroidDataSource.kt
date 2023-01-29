package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.OneToOneAndroidDataSource
import com.vgleadsheets.conversion.android.converter.GameAliasConverter
import com.vgleadsheets.conversion.android.converter.GameConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.GameAliasRoomDao
import com.vgleadsheets.database.android.dao.GameRoomDao
import com.vgleadsheets.database.android.enitity.GameAliasEntity
import com.vgleadsheets.database.android.enitity.GameEntity
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.alias.GameAlias

@Suppress("MaxLineLength")
class GameAliasAndroidDataSource(
    private val convert: GameAliasConverter,
    private val roomImpl: GameAliasRoomDao,
    private val otoRelatedRoomImpl: GameRoomDao,
    private val gameConverter: GameConverter,
) : OneToOneAndroidDataSource<GameAliasRoomDao, GameAlias, GameAliasEntity, Game, GameEntity, GameRoomDao, GameAliasConverter, GameConverter>(
    convert,
    gameConverter,
    roomImpl,
    otoRelatedRoomImpl
),
    GameAliasDataSource {
    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList {
            convert.entityToModelWithForeignOne(
                it,
                otoRelatedRoomImpl,
                gameConverter
            )
        }

    override fun getAll(withRelated: Boolean) = roomImpl
        .getAll()
        .mapList {
            convert.entityToModelWithForeignOne(
                it,
                otoRelatedRoomImpl,
                gameConverter
            )
        }
}
