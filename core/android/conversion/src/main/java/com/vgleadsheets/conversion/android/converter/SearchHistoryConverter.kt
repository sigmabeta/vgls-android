package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.entity.SearchHistoryEntryEntity
import com.vgleadsheets.model.history.SearchHistoryEntry

class SearchHistoryConverter : Converter<SearchHistoryEntry, SearchHistoryEntryEntity> {
    override fun SearchHistoryEntry.toEntity() = SearchHistoryEntryEntity(
        id,
        query,
        timeMs,
    )

    override fun SearchHistoryEntryEntity.toModel() = SearchHistoryEntry(
        id,
        query,
        timeMs
    )
}
