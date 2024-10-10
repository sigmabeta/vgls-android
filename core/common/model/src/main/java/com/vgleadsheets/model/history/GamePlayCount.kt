package com.vgleadsheets.model.history

data class GamePlayCount(
    val id: Long,
    val playCount: Int,
    val mostRecentPlay: Long,
)
