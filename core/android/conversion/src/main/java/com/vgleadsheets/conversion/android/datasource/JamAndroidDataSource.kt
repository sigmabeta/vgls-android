package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.OneToOneAndroidDataSource
import com.vgleadsheets.conversion.android.converter.JamConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.JamRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.JamEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.dao.JamDataSource
import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.Song
import kotlinx.coroutines.flow.map

class JamAndroidDataSource(
    private val convert: JamConverter,
    private val roomImpl: JamRoomDao,
    private val otoRelatedRoomImpl: SongRoomDao,
    private val songConverter: SongConverter,
) : OneToOneAndroidDataSource<JamRoomDao, Jam, JamEntity, Song, SongEntity, SongRoomDao, JamConverter, SongConverter>(
    convert,
    songConverter,
    roomImpl,
    otoRelatedRoomImpl
),
    JamDataSource {
    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }

    override fun remove(id: Long) = roomImpl.remove(id)

    override fun getOneById(id: Long) = roomImpl
        .getOneById(id)
        .map {
            convert.toModelFull(
                it,
                otoRelatedRoomImpl,
                songConverter,
            )
        }

    override fun getOneByIdSync(id: Long) = roomImpl
        .getOneByIdSync(id)
        .let {
            convert.toModelFull(
                it,
                otoRelatedRoomImpl,
                songConverter,
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
