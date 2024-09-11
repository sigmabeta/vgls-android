package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.SongPlayCountConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.SongPlayCountRoomDao
import com.vgleadsheets.database.source.SongPlayCountDataSource
import kotlinx.coroutines.flow.map

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

    override fun getPlayCount(songId: Long) = roomImpl
        .getOneById(songId)
        .map { convert.entityToModel(it ?: return@map null) }

    override fun getMostPlays() = roomImpl
        .getMostPlays()
        .mapListTo {
            convert.entityToModel(it)
        }
}
