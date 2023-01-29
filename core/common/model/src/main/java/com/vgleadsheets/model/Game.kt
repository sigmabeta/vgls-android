package com.vgleadsheets.model

data class Game(
    val id: Long,
    val name: String,
    val songs: List<Song>?,
    val photoUrl: String?,
    val sheetsPlayed: Int
)
