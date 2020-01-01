package com.vgleadsheets.model.jam

import com.vgleadsheets.model.song.Song

data class SetlistEntry(
    val id: Long,
    val gameName: String,
    val songName: String,
    val song: Song?
)
