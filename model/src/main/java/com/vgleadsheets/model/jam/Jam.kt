package com.vgleadsheets.model.jam

import com.vgleadsheets.model.song.Song

data class Jam(
    val id: Long,
    val name: String,
    val currentSong: Song,
    val songHistory: List<SongHistoryEntry>?
)
