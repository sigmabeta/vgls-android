package com.vgleadsheets.conversion.android.datasource

import com.vgleadsheets.conversion.android.converter.SearchHistoryConverter
import com.vgleadsheets.conversion.mapListTo
import com.vgleadsheets.database.android.dao.SearchHistoryEntryRoomDao
import com.vgleadsheets.database.android.enitity.DeletionId
import com.vgleadsheets.database.source.SearchHistoryDataSource
import com.vgleadsheets.model.history.SearchHistoryEntry
import kotlinx.coroutines.flow.Flow

class SearchHistoryAndroidDataSource(
    private val roomImpl: SearchHistoryEntryRoomDao,
    private val convert: SearchHistoryConverter
) : SearchHistoryDataSource {

    override suspend fun add(model: SearchHistoryEntry) = roomImpl.insert(
        convert.modelToEntity(model)
    )

    override suspend fun removeEntry(id: Long) = roomImpl.remove(
        listOf(
            DeletionId(id)
        )
    )

    override fun getRecentEntries(): Flow<List<SearchHistoryEntry>> = roomImpl
        .getRecentEntries()
        .mapListTo {
            convert.entityToModel(it)
        }
}
