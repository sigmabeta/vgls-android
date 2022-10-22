package com.vgleadsheets.conversion.wrapper

import com.vgleadsheets.conversion.OneToManyWrapper
import com.vgleadsheets.conversion.converter.TagKeyConverter
import com.vgleadsheets.conversion.converter.TagValueConverter
import com.vgleadsheets.database.android.dao.TagKeyRoomDao
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.enitity.TagKeyEntity
import com.vgleadsheets.database.enitity.TagValueEntity
import com.vgleadsheets.database.mapList
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue

class TagKeyRoomDaoWrapper(
    private val convert: TagKeyConverter,
    private val manyConverter: TagValueConverter,
    private val roomImpl: TagKeyRoomDao,
    private val relatedRoomImpl: TagValueRoomDao
) : OneToManyWrapper<TagKeyRoomDao, TagKey, TagKeyEntity, TagValue, TagValueEntity, TagValueRoomDao, TagKeyConverter, TagValueConverter>(
    convert,
    manyConverter,
    roomImpl,
    relatedRoomImpl
) {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
