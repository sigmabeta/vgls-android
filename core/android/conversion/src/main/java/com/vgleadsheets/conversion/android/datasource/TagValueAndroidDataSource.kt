package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.OneToManyAndroidDataSource
import com.vgleadsheets.conversion.android.converter.SongConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue

class TagValueAndroidDataSource(
    private val convert: TagValueConverter,
    private val manyConverter: SongConverter,
    private val roomImpl: TagValueRoomDao,
    private val relatedRoomImpl: SongRoomDao
) : OneToManyAndroidDataSource<TagValueRoomDao, TagValue, TagValueEntity, Song, SongEntity, SongRoomDao, TagValueConverter, SongConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
) {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
