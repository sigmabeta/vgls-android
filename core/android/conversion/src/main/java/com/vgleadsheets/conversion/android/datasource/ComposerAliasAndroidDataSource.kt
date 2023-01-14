package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.OneToOneAndroidDataSource
import com.vgleadsheets.conversion.android.converter.ComposerAliasConverter
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.ComposerAliasRoomDao
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.enitity.ComposerAliasEntity
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.dao.ComposerAliasDataSource
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.alias.ComposerAlias

class ComposerAliasAndroidDataSource(
    private val convert: ComposerAliasConverter,
    private val roomImpl: ComposerAliasRoomDao,
    private val otoRelatedRoomImpl: ComposerRoomDao,
    private val composerConverter: ComposerConverter,
) : OneToOneAndroidDataSource<ComposerAliasRoomDao, ComposerAlias, ComposerAliasEntity, Composer, ComposerEntity, ComposerRoomDao, ComposerAliasConverter, ComposerConverter>(
    convert,
    composerConverter,
    roomImpl,
    otoRelatedRoomImpl
), ComposerAliasDataSource {
    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList {
            convert.entityToModelWithForeignOne(
                it,
                otoRelatedRoomImpl,
                composerConverter
            )
        }

    override fun getAll(withRelated: Boolean) = roomImpl
        .getAll()
        .mapList {
            convert.entityToModelWithForeignOne(
                it,
                otoRelatedRoomImpl,
                composerConverter
            )
        }
}
