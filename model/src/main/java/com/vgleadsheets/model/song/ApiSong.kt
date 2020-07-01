package com.vgleadsheets.model.song

import com.vgleadsheets.model.composer.ApiSongComposer

data class ApiSong(
    val id: Long,
    val filename: String,
    val parts: Set<String>,
    val name: String,
    val pageCount: Int,
    val lyricsPageCount: Int,
    val composers: List<ApiSongComposer>,
    val tags: Map<String, List<String>>
) {
    fun toSongEntity(gameId: Long, gameName: String) = SongEntity(id,
        name,
        pageCount,
        gameName,
        gameId)
}
