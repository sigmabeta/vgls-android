package com.vgleadsheets.model

data class Composer(
    val id: Long,
    val name: String,
    val songs: List<Song>?,
    val photoUrl: String?,
    val hasVocalSongs: Boolean
)
