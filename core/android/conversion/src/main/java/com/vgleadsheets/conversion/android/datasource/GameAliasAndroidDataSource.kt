package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.AndroidDataSource
import com.vgleadsheets.conversion.android.converter.GameAliasConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.GameAliasRoomDao
import com.vgleadsheets.database.android.enitity.GameAliasEntity
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.model.alias.GameAlias

@Suppress("MaxLineLength")
class GameAliasAndroidDataSource(
    private val convert: GameAliasConverter,
    private val roomImpl: GameAliasRoomDao,
) : AndroidDataSource<GameAliasRoomDao, GameAlias, GameAliasEntity, GameAliasConverter>(
    convert,
    roomImpl,
),
    GameAliasDataSource {
    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapListTo {
            convert.entityToModel(it)
        }

    override fun getAll() = roomImpl
        .getAll()
        .mapListTo {
            convert.entityToModel(it)
        }
}
