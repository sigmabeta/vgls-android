package com.vgleadsheets.features.main.hud

import com.vgleadsheets.mvrx.MvRxViewModel

class HudViewModel(initialState: HudState) : MvRxViewModel<HudState>(initialState) {
    fun searchClick() {
        setState { copy(searchVisible = true) }
    }

    fun searchQuery(query: String?) {
        setState { copy(searchQuery = query) }
    }
}
