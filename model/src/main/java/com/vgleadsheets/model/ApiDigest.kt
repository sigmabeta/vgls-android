package com.vgleadsheets.model

import com.vgleadsheets.model.composer.ApiComposer
import com.vgleadsheets.model.game.VglsApiGame

data class ApiDigest(
    val composers: List<ApiComposer>,
    val games: List<VglsApiGame>
)
