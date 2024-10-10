package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.TagValuePlayCountConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.TagValuePlayCountRoomDao
import com.vgleadsheets.database.source.TagValuePlayCountDataSource

class TagValuePlayCountAndroidDataSource(
    private val roomImpl: TagValuePlayCountRoomDao,
    private val convert: TagValuePlayCountConverter
) : TagValuePlayCountDataSource {
    override suspend fun incrementPlayCount(
        tagValueId: Long,
        mostRecentPlay: Long,
    ) = roomImpl.incrementPlayCount(
        tagValueId,
        mostRecentPlay,
    )

    override fun getMostPlays() = roomImpl
        .getMostPlays()
        .mapListTo {
            convert.entityToModel(it)
        }
}
