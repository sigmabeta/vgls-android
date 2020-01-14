package com.vgleadsheets.model.jam

@Suppress("ConstructorParameterNaming")
data class ApiSongHistoryEntry(
    val sheet_id: Long
) {
    fun toSongHistoryEntryEntity(jamId: Long, listPosition: Int) = SongHistoryEntryEntity(
        ApiSetlistEntry.MULTIPLIER_JAM_ID * jamId + listPosition,
        jamId,
        sheet_id
    )

    companion object {
        const val MULTIPLIER_JAM_ID = 10000L
    }
}
