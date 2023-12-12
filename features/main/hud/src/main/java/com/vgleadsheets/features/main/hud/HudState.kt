package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.hud.search.SearchContent

data class HudState(
    val searchQuery: String? = null,
    val searchResults: SearchContent = SearchContent(
        Uninitialized,
        Uninitialized,
        Uninitialized,
        false
    ),
) : MavericksState
