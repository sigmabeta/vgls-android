package com.vgleadsheets.model.history

data class SearchHistoryEntry(
    val id: Long? = null,
    val query: String,
    val timeMs: Long,
)
