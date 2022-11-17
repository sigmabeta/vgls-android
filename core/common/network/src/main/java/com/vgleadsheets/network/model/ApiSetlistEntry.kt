package com.vgleadsheets.network.model

@Suppress("ConstructorParameterNaming")
data class ApiSetlistEntry(
    val id: Long,
    val game_name: String,
    val song_name: String
)
