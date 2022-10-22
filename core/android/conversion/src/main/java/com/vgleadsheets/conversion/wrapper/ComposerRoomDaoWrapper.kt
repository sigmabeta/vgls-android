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

class ComposerRoomDaoWrapper(
    private val convert: ComposerConverter,
    private val manyConverter: SongConverter,
    private val roomImpl: ComposerRoomDao,
    private val relatedRoomImpl: SongRoomDao
) : ManyToManyWrapper<ComposerRoomDao, Composer, ComposerEntity, Song, SongEntity, ComposerRoomDao, SongRoomDao, ComposerConverter, SongConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
) {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
