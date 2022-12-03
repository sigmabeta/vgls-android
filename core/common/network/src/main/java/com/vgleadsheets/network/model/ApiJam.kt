package com.vgleadsheets.network.model

@Suppress("ConstructorParameterNaming")
data class ApiJam(
    val jam_id: Long,
    val song_history: List<ApiSongHistoryEntry>
)
