package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.ComposerPlayCountEntity
import com.vgleadsheets.model.history.ComposerPlayCount

class ComposerPlayCountConverter : Converter<ComposerPlayCount, ComposerPlayCountEntity> {
    override fun ComposerPlayCount.toEntity() = ComposerPlayCountEntity(
        id,
        playCount,
        mostRecentPlay,
    )

    override fun ComposerPlayCountEntity.toModel() = ComposerPlayCount(
        id,
        playCount,
        mostRecentPlay,
    )
}
