package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.ComposerPlayCountConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.ComposerPlayCountRoomDao
import com.vgleadsheets.database.source.ComposerPlayCountDataSource

class ComposerPlayCountAndroidDataSource(
    private val roomImpl: ComposerPlayCountRoomDao,
    private val convert: ComposerPlayCountConverter
) : ComposerPlayCountDataSource {
    override suspend fun incrementPlayCount(
        composerId: Long,
        mostRecentPlay: Long,
    ) = roomImpl.incrementPlayCount(
        composerId,
        mostRecentPlay,
    )

    override fun getMostPlays() = roomImpl
        .getMostPlays()
        .mapListTo {
            convert.entityToModel(it)
        }
}
