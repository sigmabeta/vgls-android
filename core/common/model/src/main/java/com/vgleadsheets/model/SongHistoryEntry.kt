package com.vgleadsheets.model

data class SongHistoryEntry(
    val id: Long,
    val songId: Long,
    val jamId: Long,
    val song: Song?
)
