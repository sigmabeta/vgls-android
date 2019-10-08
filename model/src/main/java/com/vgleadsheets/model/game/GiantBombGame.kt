package com.vgleadsheets.model.game

import com.vgleadsheets.model.giantbomb.GiantBombImage

data class GiantBombGame(
    val id: Long,
    val name: String,
    val image: GiantBombImage
)
