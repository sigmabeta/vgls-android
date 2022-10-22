package com.vgleadsheets.conversion.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToManyConverter
import com.vgleadsheets.database.android.dao.TagValueRoomDao
import com.vgleadsheets.database.enitity.TagKeyEntity
import com.vgleadsheets.database.enitity.TagValueEntity
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue

class TagKeyConverter :
    OneToManyConverter<TagKey, TagKeyEntity, TagValue, TagValueEntity, TagValueRoomDao> {
    override fun TagKey.toEntity() = TagKeyEntity(
        id,
        name,
    )

    override fun TagKeyEntity.toModel() = TagKey(
        id,
        name,
        null,
    )

    override fun TagKeyEntity.toModelWithRelatedMany(
        manyDao: TagValueRoomDao,
        converter: Converter<TagValue, TagValueEntity>
    ): TagKey = TagKey(
        id,
        name,
        manyDao.getRelatedModels(id, converter),
    )

    override fun TagValueRoomDao.getRelatedModels(
        relationId: Long,
        converter: Converter<TagValue, TagValueEntity>
    ) = getRelatedEntitiesSync(relationId).map { converter.entityToModel(it) }
}
