package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.AndroidDataSource
import com.vgleadsheets.conversion.android.converter.ComposerAliasConverter
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.enitity.ComposerAliasEntity
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.model.alias.ComposerAlias

@Suppress("MaxLineLength")
class ComposerAliasAndroidDataSource(
    private val convert: ComposerAliasConverter,
    private val roomImpl: ComposerAliasRoomDao,
    private val otoRelatedRoomImpl: ComposerRoomDao,
    private val composerConverter: ComposerConverter,
) : AndroidDataSource<ComposerAliasRoomDao, ComposerAlias, ComposerAliasEntity, ComposerAliasConverter>(
    convert,
    roomImpl,
),
    ComposerAliasDataSource {
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
