package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.RegularAndroidDataSource
import com.vgleadsheets.conversion.android.converter.ComposerAliasConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.android.enitity.ComposerAliasEntity
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.model.alias.ComposerAlias
import javax.inject.Inject

class ComposerAliasAndroidDataSource @Inject constructor(
    private val convert: ComposerAliasConverter,
    private val roomImpl: ComposerAliasRoomDao
) : RegularAndroidDataSource<ComposerAliasRoomDao, ComposerAlias, ComposerAliasEntity, ComposerAliasConverter>(
    convert,
    roomImpl
), ComposerAliasDataSource {
    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
