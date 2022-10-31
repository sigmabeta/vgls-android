package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.ManyToManyAndroidDataSource
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song

class ComposerAndroidDataSource(
    private val convert: ComposerConverter,
    private val manyConverter: SongConverter,
    private val roomImpl: ComposerRoomDao,
    private val relatedRoomImpl: SongRoomDao
) : ManyToManyAndroidDataSource<ComposerRoomDao, Composer, ComposerEntity, Song, SongEntity, ComposerRoomDao, SongRoomDao, ComposerConverter, SongConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
) {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
