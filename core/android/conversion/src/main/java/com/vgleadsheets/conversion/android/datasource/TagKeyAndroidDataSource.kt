package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.AndroidDataSource
import com.vgleadsheets.conversion.android.converter.TagKeyConverter
import com.vgleadsheets.conversion.mapList
import com.vgleadsheets.database.android.dao.TagKeyRoomDao
import com.vgleadsheets.database.android.enitity.TagKeyEntity
import com.vgleadsheets.database.dao.TagKeyDataSource
import com.vgleadsheets.model.tag.TagKey

class TagKeyAndroidDataSource(
    private val convert: TagKeyConverter,
    private val roomImpl: TagKeyRoomDao,
) : AndroidDataSource<TagKeyRoomDao, TagKey, TagKeyEntity, TagKeyConverter>(
    convert,
    roomImpl,
), TagKeyDataSource {
    fun searchByName(name: String) = roomImpl
        .searchByName(name)
        .mapList { convert.entityToModel(it) }
}
