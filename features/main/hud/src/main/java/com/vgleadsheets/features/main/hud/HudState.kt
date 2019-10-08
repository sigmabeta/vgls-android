package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.game.VglsApiGame

data class HudState(
    val menuExpanded: Boolean = false,
    val hudVisible: Boolean = true,
    val searchVisible: Boolean = false,
    val searchQuery: String? = null,
    val parts: List<PartSelectorItem>? = null,
    val updateTime: Async<Long> = Uninitialized,
    val digest: Async<List<VglsApiGame>> = Uninitialized
) : MvRxState
