package com.vgleadsheets.conversion.wrapper

import com.vgleadsheets.conversion.ManyToManyWrapper
import com.vgleadsheets.conversion.converter.ComposerConverter
import com.vgleadsheets.conversion.converter.SongConverter
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.enitity.ComposerEntity
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.database.mapList
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song

class SongRoomDaoWrapper(
    private val convert: SongConverter,
    private val manyConverter: ComposerConverter,
    private val roomImpl: SongRoomDao,
    private val relatedRoomImpl: ComposerRoomDao
) : ManyToManyWrapper<SongRoomDao, Song, SongEntity, Composer, ComposerEntity, SongRoomDao, ComposerRoomDao, SongConverter, ComposerConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
) {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.toModelFromEntity(it) }
}
