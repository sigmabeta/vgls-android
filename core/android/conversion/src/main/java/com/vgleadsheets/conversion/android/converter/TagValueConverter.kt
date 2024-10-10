package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.TagValueEntity
import com.vgleadsheets.model.tag.TagValue

class TagValueConverter :
    Converter<TagValue, TagValueEntity> {
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
}
