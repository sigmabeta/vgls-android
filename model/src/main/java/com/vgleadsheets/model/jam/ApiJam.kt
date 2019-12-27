package com.vgleadsheets.model.jam

@Suppress("ConstructorParameterNaming")
data class ApiJam(
    val jam_id: Long,
    val song_history: List<ApiSongHistoryEntry>
) {
    fun toJamEntity(name: String) = JamEntity(jam_id, name, song_history[0].sheet_id)
}