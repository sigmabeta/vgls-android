package com.vgleadsheets.network.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiSong(
    val id: Long,
    val filename: String,
    val parts: Set<String>,
    val name: String,
    val pageCount: Int,
    val altPageCount: Int,
    val lyricsPageCount: Int,
    val composers: List<ApiComposer>,
    val tags: Map<String, List<String>>,
    val aliases: List<String>?
)
