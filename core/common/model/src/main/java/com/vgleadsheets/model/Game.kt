package com.vgleadsheets.model

data class Game(
    val id: Long,
    val name: String,
    val songs: List<Song>?,
    val hasVocalSongs: Boolean,
    val songCount: Int,
    val photoUrl: String?,
    val sheetsPlayed: Int,
    val isFavorite: Boolean,
    val isAvailableOffline: Boolean,
)
