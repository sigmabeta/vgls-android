package com.vgleadsheets.network.model

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
    fun toEntity(
        id,
        name,
        filename,
        pageCount,
        lyricsPageCount,
        gameName,
        gameId
    )
}
