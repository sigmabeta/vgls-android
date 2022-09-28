package com.vgleadsheets.database.model

import com.vgleadsheets.model.song.Song

data class SongHistoryEntry(
    val id: Long,
    val song: Song?
)
