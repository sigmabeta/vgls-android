package com.vgleadsheets.model.game

import com.vgleadsheets.model.song.Song

data class Game(
    val id: Long,
    val name: String,
    val songs: List<Song>?,
    val photoUrl: String?
)
