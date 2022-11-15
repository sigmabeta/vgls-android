package com.vgleadsheets.model

data class Jam(
    val id: Long,
    val name: String,
    val currentSongId: Long?,
    val currentSong: Song?
)
