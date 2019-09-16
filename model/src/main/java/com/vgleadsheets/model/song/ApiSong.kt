package com.vgleadsheets.model.song

import com.vgleadsheets.model.composer.ApiComposer
import com.vgleadsheets.model.files.ApiFiles

data class ApiSong(
    val id: Long,
    val filename: String,
    val files: ApiFiles,
    val name: String,
    val pageCount: Int,
    val lyricsPageCount: Int,
    val composers: List<ApiComposer>,
    val tags: Map<String, List<Any>>
) {
    fun toSongEntity(gameId: Long) = SongEntity(id,
        filename,
        name,
        pageCount,
        gameId)
}
