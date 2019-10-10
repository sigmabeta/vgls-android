package com.vgleadsheets.model.search

data class SearchResult(
    val id: Long,
    val type: SearchResultType,
    val name: String,
    val giantBombId: Long?,
    val imageUrl: String?
)
