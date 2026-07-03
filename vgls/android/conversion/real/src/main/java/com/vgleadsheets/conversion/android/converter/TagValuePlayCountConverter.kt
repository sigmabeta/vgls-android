package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.TagValuePlayCountEntity
import com.vgleadsheets.model.history.TagValuePlayCount

class TagValuePlayCountConverter : Converter<TagValuePlayCount, TagValuePlayCountEntity> {
    override fun TagValuePlayCount.toEntity() = TagValuePlayCountEntity(
        id,
        playCount,
        mostRecentPlay,
    )

    override fun TagValuePlayCountEntity.toModel() = TagValuePlayCount(
        id,
        playCount,
        mostRecentPlay,
    )
}
