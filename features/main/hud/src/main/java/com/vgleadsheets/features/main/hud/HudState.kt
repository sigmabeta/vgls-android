package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.MvRxState
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem

data class HudState(
    val menuExpanded: Boolean = false,
    val hudVisible: Boolean = true,
    val searchVisible: Boolean = false,
    val searchQuery: String? = null,
    val parts: List<PartSelectorItem>? = null
) : MvRxState
