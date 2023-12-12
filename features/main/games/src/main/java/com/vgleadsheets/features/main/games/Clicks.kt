package com.vgleadsheets.features.main.games

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.Navigator

class Clicks(
    private val navigator: Navigator
) : ListItemClicks {
    fun game(id: Long) {
        navigator.showGameDetail(id)
    }
}
