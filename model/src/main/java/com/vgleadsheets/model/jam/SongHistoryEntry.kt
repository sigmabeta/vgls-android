package com.vgleadsheets.model.jam

import com.vgleadsheets.model.song.Song

data class SongHistoryEntry(
    val id: Long,
    val song: Song?
)
