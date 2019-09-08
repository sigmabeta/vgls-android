package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.MvRxState

data class HudState(
    val hudVisible: Boolean = true,
    val searchVisible: Boolean = false,
    val searchQuery: String? = null
) : MvRxState