package com.vgleadsheets.conversion.wrapper

import com.vgleadsheets.conversion.OneToManyWrapper
import com.vgleadsheets.conversion.converter.SongConverter
import com.vgleadsheets.conversion.converter.TagValueConverter
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.database.enitity.TagValueEntity
import com.vgleadsheets.database.mapList
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue

class TagValueRoomDaoWrapper(
    private val convert: TagValueConverter,
    private val manyConverter: SongConverter,
    private val roomImpl: TagValueRoomDao,
    private val relatedRoomImpl: SongRoomDao
) : OneToManyWrapper<TagValueRoomDao, TagValue, TagValueEntity, Song, SongEntity, SongRoomDao, TagValueConverter, SongConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
) {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
