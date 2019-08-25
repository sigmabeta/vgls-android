package com.vgleadsheets.model.song

import com.vgleadsheets.model.composer.ApiComposer

data class ApiSong(
    val id: Long,
    val filename: String,
    val name: String,
    val pageCount: Int,
    val composers: List<ApiComposer>,
    val tags: Map<String, List<Any>>
) {
    fun toSongEntity(gameId: Long) = SongEntity(id,
        filename,
        name,
        pageCount,
        gameId)
}
