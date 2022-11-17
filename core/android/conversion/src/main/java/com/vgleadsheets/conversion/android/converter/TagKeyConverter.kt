package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.WithManyConverter
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.android.enitity.TagKeyEntity
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue

class TagKeyConverter :
    WithManyConverter<TagKey, TagKeyEntity, TagValue, TagValueEntity, TagValueRoomDao> {
    override fun TagKey.toEntity() = TagKeyEntity(
        id,
        name,
    )

    override fun TagKeyEntity.toModel() = TagKey(
        id,
        name,
        null,
    )

    override fun TagKeyEntity.toModelWithMany(
        manyDao: TagValueRoomDao,
        converter: Converter<TagValue, TagValueEntity>
    ): TagKey = TagKey(
        id,
        name,
        manyDao.getManyModels(id, converter),
    )

    override fun TagValueRoomDao.getManyModels(
        relationId: Long,
        converter: Converter<TagValue, TagValueEntity>
    ) = getEntitiesForForeignSync(relationId).map { converter.entityToModel(it) }
}
