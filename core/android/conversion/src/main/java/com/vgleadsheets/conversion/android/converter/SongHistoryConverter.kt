package com.vgleadsheets.conversion.android.converter

import com.vgleadsheets.conversion.Converter
import com.vgleadsheets.database.android.entity.SongHistoryEntryEntity
import com.vgleadsheets.model.history.SongHistoryEntry

class SongHistoryConverter : Converter<SongHistoryEntry, SongHistoryEntryEntity> {
    override fun SongHistoryEntry.toEntity() = SongHistoryEntryEntity(
        songId,
        timeMs,
        id,
    )

    override fun SongHistoryEntryEntity.toModel() = SongHistoryEntry(
        songId,
        timeMs,
        id
    )
}
