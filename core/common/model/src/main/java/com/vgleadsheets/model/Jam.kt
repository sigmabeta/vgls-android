package com.vgleadsheets.model

data class Jam(
    val id: Long,
    val name: String,
    val currentSong: Song?,
    val songHistory: List<SongHistoryEntry>?
)
