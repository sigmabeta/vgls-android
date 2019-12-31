package com.vgleadsheets.model.jam

@Suppress("ConstructorParameterNaming")
data class ApiSetlistEntry(
    val id: Long,
    val game_name: String,
    val song_name: String
) {
    fun toSetlistEntryEntity(jamId: Long) = SetlistEntryEntity(
        id,
        game_name,
        song_name,
        jamId
    )
}
