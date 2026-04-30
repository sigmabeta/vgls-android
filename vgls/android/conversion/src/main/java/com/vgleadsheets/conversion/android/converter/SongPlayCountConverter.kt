package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.SongPlayCountEntity
import com.vgleadsheets.model.history.SongPlayCount

class SongPlayCountConverter : Converter<SongPlayCount, SongPlayCountEntity> {
    override fun SongPlayCount.toEntity() = SongPlayCountEntity(
        id,
        playCount,
        mostRecentPlay,
    )

    override fun SongPlayCountEntity.toModel() = SongPlayCount(
        id,
        playCount,
        mostRecentPlay,
    )
}
