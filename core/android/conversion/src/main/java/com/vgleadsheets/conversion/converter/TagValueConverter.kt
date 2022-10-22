package com.vgleadsheets.conversion.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.OneToManyConverter
import com.vgleadsheets.database.android.dao.SongRoomDao
import com.vgleadsheets.database.enitity.SongEntity
import com.vgleadsheets.database.enitity.TagValueEntity
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue

class TagValueConverter :
    OneToManyConverter<TagValue, TagValueEntity, Song, SongEntity, SongRoomDao> {
    override fun TagValue.toEntity() = TagValueEntity(
        id,
        name,
        tagKeyId,
        tagKeyName
    )

    override fun TagValueEntity.toModel() = TagValue(
        id,
        name,
        tag_key_id,
        tag_key_name,
        null
    )

    override fun TagValueEntity.toModelWithRelatedMany(
        manyDao: SongRoomDao,
        converter: Converter<Song, SongEntity>
    ): TagValue = TagValue(
        id,
        name,
        tag_key_id,
        tag_key_name,
        manyDao.getRelatedModels(id, converter),
    )

    override fun SongRoomDao.getRelatedModels(
        relationId: Long,
        converter: Converter<Song, SongEntity>
    ) = getRelatedEntitiesSync(relationId).map { converter.entityToModel(it) }
}
