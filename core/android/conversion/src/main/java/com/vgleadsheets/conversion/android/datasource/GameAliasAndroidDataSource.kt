package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.RegularAndroidDataSource
import com.vgleadsheets.conversion.android.converter.GameAliasConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.GameAliasRoomDao
import com.vgleadsheets.database.android.enitity.GameAliasEntity
import com.vgleadsheets.database.dao.GameAliasDataSource
import com.vgleadsheets.model.alias.GameAlias
import javax.inject.Inject

class GameAliasAndroidDataSource @Inject constructor(
    private val convert: GameAliasConverter,
    private val roomImpl: GameAliasRoomDao,
) : RegularAndroidDataSource<GameAliasRoomDao, GameAlias, GameAliasEntity, GameAliasConverter>(
    convert,
    roomImpl
), GameAliasDataSource {
    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
