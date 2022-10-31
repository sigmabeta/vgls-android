package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.OneToManyAndroidDataSource
import com.vgleadsheets.conversion.android.converter.TagKeyConverter
import com.vgleadsheets.conversion.android.converter.TagValueConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.TagKeyRoomDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.android.enitity.TagKeyEntity
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue

class TagKeyAndroidDataSource(
    private val convert: TagKeyConverter,
    private val manyConverter: TagValueConverter,
    private val roomImpl: TagKeyRoomDao,
    private val relatedRoomImpl: TagValueRoomDao
) : OneToManyAndroidDataSource<TagKeyRoomDao, TagKey, TagKeyEntity, TagValue, TagValueEntity, TagValueRoomDao, TagKeyConverter, TagValueConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
) {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
