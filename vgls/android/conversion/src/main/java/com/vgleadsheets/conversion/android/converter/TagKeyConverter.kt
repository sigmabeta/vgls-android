package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.TagKeyEntity
import com.vgleadsheets.model.tag.TagKey

class TagKeyConverter : Converter<TagKey, TagKeyEntity> {
    override fun TagKey.toEntity() = TagKeyEntity(
        id,
        name,
    )

    override fun TagKeyEntity.toModel() = TagKey(
        id,
        name,
        null,
    )
}
