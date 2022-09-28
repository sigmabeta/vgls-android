package com.vgleadsheets.model.jam

@Suppress("ConstructorParameterNaming")
data class ApiJam(
    val jam_id: Long,
    val song_history: List<com.vgleadsheets.database.model.ApiSongHistoryEntry>
) {
    fun toJamEntity(name: String) = com.vgleadsheets.database.model.JamEntity(
        jam_id,
        name,
        song_history
            .firstOrNull()
            ?.sheet_id
    )
}
