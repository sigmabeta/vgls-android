package com.vgleadsheets.model.alias

import com.vgleadsheets.model.Game

data class GameAlias(
    val id: Long?,
    val gameId: Long,
    val name: String,
    val game: Game?
)
