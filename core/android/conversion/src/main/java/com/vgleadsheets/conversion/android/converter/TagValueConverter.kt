package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.conversion.WithManyConverter
import com.vgleadsheets.database.android.dao.SongsForTagValueDao
import com.vgleadsheets.database.android.enitity.SongEntity
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.tag.TagValue

class TagValueConverter :
    WithManyConverter<TagValue, TagValueEntity, Song, SongEntity, SongsForTagValueDao> {
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

    override fun TagValueEntity.toModelWithMany(
        manyDao: SongsForTagValueDao,
        converter: Converter<Song, SongEntity>
    ): TagValue = TagValue(
        id,
        name,
        tag_key_id,
        tag_key_name,
        manyDao.getManyModels(id, converter),
    )

    override fun SongsForTagValueDao.getManyModels(
        relationId: Long,
        converter: Converter<Song, SongEntity>
    ) = getJoinedEntitiesSync(relationId).map { converter.entityToModel(it) }
}
