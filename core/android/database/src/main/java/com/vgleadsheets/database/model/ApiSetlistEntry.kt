package com.vgleadsheets.database.model

@Suppress("ConstructorParameterNaming")
data class ApiSetlistEntry(
    val id: Long,
    val game_name: String,
    val song_name: String
) {
    fun toSetlistEntryEntity(jamId: Long, listPosition: Int) = SetlistEntryEntity(
        MULTIPLIER_JAM_ID * jamId + listPosition,
        game_name,
        song_name,
        jamId,
        id
    )

    companion object {
        const val MULTIPLIER_JAM_ID = 100000L
    }
}
