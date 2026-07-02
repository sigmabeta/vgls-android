package com.vgleadsheets.model.history

data class SongPlayCount(
    val id: Long,
    val playCount: Int,
    val mostRecentPlay: Long,
)
