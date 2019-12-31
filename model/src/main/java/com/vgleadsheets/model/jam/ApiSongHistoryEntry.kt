package com.vgleadsheets.model.jam

@Suppress("ConstructorParameterNaming")
data class ApiSongHistoryEntry(
    val sheet_id: Long
) {
    fun toSongHistoryEntryEntity(jamId: Long) = SongHistoryEntryEntity(
        null,
        jamId,
        sheet_id
    )
}
