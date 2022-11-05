package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.ManyToManyAndroidDataSource
import com.vgleadsheets.conversion.android.converter.ComposerConverter
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.ComposerRoomDao
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.android.enitity.ComposerEntity
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.dao.SongDataSource
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Song
import javax.inject.Inject

class SongAndroidDataSource @Inject constructor(
    private val convert: SongConverter,
    private val manyConverter: ComposerConverter,
    private val tagValueConverter: TagValueConverter,
    private val roomImpl: SongRoomDao,
    private val relatedRoomImpl: ComposerRoomDao,
    private val tagValueRoomImpl: TagValueRoomDao
) : ManyToManyAndroidDataSource<SongRoomDao, Song, SongEntity, Composer, ComposerEntity, SongRoomDao, ComposerRoomDao, SongConverter, ComposerConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
), SongDataSource {
    override fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.toModelFromEntity(it) }

    override fun getTagValuesForSong(songId: Long) = tagValueRoomImpl
        .getJoinedEntities(songId)
        .mapList {
            tagValueConverter.entityToModel(it)
        }
}
