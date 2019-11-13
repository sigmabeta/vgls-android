package com.vgleadsheets.model.tag

import com.vgleadsheets.model.song.Song

data class TagValue(
    val id: Long,
    val name: String,
    val songs: List<Song>
)
