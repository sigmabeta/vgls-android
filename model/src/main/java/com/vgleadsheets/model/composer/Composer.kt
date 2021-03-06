package com.vgleadsheets.model.composer

import com.vgleadsheets.model.song.Song

data class Composer(
    val id: Long,
    val name: String,
    val songs: List<Song>?,
    val photoUrl: String?
)
