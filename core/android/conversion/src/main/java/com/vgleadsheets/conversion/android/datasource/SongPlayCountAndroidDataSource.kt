package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.SongPlayCountConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.SongPlayCountRoomDao
import com.vgleadsheets.database.source.SongPlayCountDataSource

class SongPlayCountAndroidDataSource(
    private val roomImpl: SongPlayCountRoomDao,
    private val convert: SongPlayCountConverter
) : SongPlayCountDataSource {
    override suspend fun incrementPlayCount(
        songId: Long,
        mostRecentPlay: Long,
    ) = roomImpl.incrementPlayCount(
        songId,
        mostRecentPlay,
    )

    override fun getAll() = roomImpl
        .getAll()
        .mapListTo {
            convert.entityToModel(it)
        }
}
