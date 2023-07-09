package com.vgleadsheets.model

data class Song(
    val id: Long,
    val name: String,
    val filename: String,
    val gameId: Long,
    val gameName: String,
    val game: Game?,
    val hasVocals: Boolean,
    val pageCount: Int,
    val altPageCount: Int,
    val lyricPageCount: Int,
    val composers: List<Composer>?,
    val playCount: Int,
    val isFavorite: Boolean,
    val isAvailableOffline: Boolean,
    val isAltSelected: Boolean,
)
