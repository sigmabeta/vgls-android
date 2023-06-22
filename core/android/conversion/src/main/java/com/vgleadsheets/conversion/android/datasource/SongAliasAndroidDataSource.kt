package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.AndroidDataSource
import com.vgleadsheets.conversion.android.converter.SongAliasConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.SongAliasRoomDao
import com.vgleadsheets.database.android.enitity.SongAliasEntity
import com.vgleadsheets.database.dao.SongAliasDataSource
import com.vgleadsheets.model.alias.SongAlias

@Suppress("MaxLineLength")
class SongAliasAndroidDataSource(
    private val convert: SongAliasConverter,
    private val roomImpl: SongAliasRoomDao,
) : AndroidDataSource<SongAliasRoomDao, SongAlias, SongAliasEntity, SongAliasConverter>(
    convert,
    roomImpl,
),
    SongAliasDataSource {
    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList {
            convert.entityToModel(it)
        }

    override fun getAll() = roomImpl
        .getAll()
        .mapList {
            convert.entityToModel(it)
        }

    override fun getAliasesForSong(songId: Long) = roomImpl
        .getForSong(songId)
        .mapList {
            convert.entityToModel(it)
        }
}
