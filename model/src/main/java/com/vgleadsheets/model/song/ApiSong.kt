package com.vgleadsheets.model.song

import com.vgleadsheets.model.composer.ApiComposer

data class ApiSong(
    val id: Long,
    val filename: String,
    val parts: Set<String>,
    val name: String,
    val pageCount: Int,
    val lyricsPageCount: Int,
    val composers: List<ApiComposer>,
    val tags: Map<String, List<String>>
) {
    fun toSongEntity(gameId: Long, gameName: String) = SongEntity(
        id,
        name,
        pageCount,
        gameName,
        gameId
    )
}
