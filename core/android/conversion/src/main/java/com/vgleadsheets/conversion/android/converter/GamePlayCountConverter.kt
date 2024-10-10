package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.enitity.GamePlayCountEntity
import com.vgleadsheets.model.history.GamePlayCount

class GamePlayCountConverter : Converter<GamePlayCount, GamePlayCountEntity> {
    override fun GamePlayCount.toEntity() = GamePlayCountEntity(
        id,
        playCount,
        mostRecentPlay,
    )

    override fun GamePlayCountEntity.toModel() = GamePlayCount(
        id,
        playCount,
        mostRecentPlay,
    )
}
