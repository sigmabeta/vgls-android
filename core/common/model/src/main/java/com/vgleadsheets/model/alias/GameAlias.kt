package com.vgleadsheets.model.alias

data class GameAlias(
    val id: Long,
    val gameId: Long,
    val name: String,
    val photoUrl: String?
)
