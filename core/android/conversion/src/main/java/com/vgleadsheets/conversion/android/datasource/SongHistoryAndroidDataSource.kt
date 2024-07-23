package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.SongHistoryConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.SongHistoryEntryRoomDao
import com.vgleadsheets.database.source.SongHistoryDataSource
import com.vgleadsheets.model.history.SongHistoryEntry

class SongHistoryAndroidDataSource(
    private val roomImpl: SongHistoryEntryRoomDao,
    private val convert: SongHistoryConverter
) : SongHistoryDataSource {
    override suspend fun insert(model: SongHistoryEntry) = roomImpl.insert(
        convert.modelToEntity(model)
    )

    override fun getRecentSongs() = roomImpl
        .getMostPlays()
        .mapListTo {
            convert.entityToModel(it)
        }
}
