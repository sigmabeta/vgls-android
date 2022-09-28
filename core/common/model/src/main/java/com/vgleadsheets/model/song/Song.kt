package com.vgleadsheets.model.song

import com.vgleadsheets.model.composer.Composer

data class Song(
    val id: Long,
    val name: String,
    val filename: String,
    val gameId: Long,
    val gameName: String,
    val hasVocals: Boolean,
    val pageCount: Int,
    val lyricPageCount: Int,
    val composers: List<Composer>?,
    val playCount: Int
)
