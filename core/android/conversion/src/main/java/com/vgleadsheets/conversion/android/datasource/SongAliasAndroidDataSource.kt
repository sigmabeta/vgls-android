package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.OneToOneAndroidDataSource
import com.vgleadsheets.conversion.android.converter.SongAliasConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.SongAliasRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.SongAliasEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.SongAlias

class SongAliasAndroidDataSource(
    private val convert: SongAliasConverter,
    private val roomImpl: SongAliasRoomDao,
    private val otoRelatedRoomImpl: SongRoomDao,
    private val songConverter: SongConverter,
) : OneToOneAndroidDataSource<SongAliasRoomDao, SongAlias, SongAliasEntity, Song, SongEntity, SongRoomDao, SongAliasConverter, SongConverter>(
    convert,
    songConverter,
    roomImpl,
    otoRelatedRoomImpl
), SongAliasDataSource {
    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList {
            convert.entityToModelWithForeignOne(
                it,
                otoRelatedRoomImpl,
                songConverter
            )
        }

    override fun getAll(withRelated: Boolean) = roomImpl
        .getAll()
        .mapList {
            convert.entityToModelWithForeignOne(
                it,
                otoRelatedRoomImpl,
                songConverter
            )
        }
}
