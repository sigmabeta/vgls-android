package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.GamePlayCountConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.GamePlayCountRoomDao
import com.vgleadsheets.database.source.GamePlayCountDataSource

class GamePlayCountAndroidDataSource(
    private val roomImpl: GamePlayCountRoomDao,
    private val convert: GamePlayCountConverter
) : GamePlayCountDataSource {
    override suspend fun incrementPlayCount(
        gameId: Long,
        mostRecentPlay: Long,
    ) = roomImpl.incrementPlayCount(
        gameId,
        mostRecentPlay,
    )

    override fun getMostPlays() = roomImpl
        .getMostPlays()
        .mapListTo {
            convert.entityToModel(it)
        }
}
