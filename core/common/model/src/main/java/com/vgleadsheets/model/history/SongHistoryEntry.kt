package com.vgleadsheets.model.history

data class SongHistoryEntry(
    val songId: Long,
    val timeMs: Long,
    val id: Long? = null,
)
