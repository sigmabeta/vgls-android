package com.vgleadsheets.features.main.favorites

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.Navigator

class Clicks(
    private val navigator: Navigator,
) : ListItemClicks {
    fun menu() {
        navigator.toMenu()
    }

    fun song(id: Long) {
        navigator.showSongViewer(id)
    }

    fun game(id: Long) {
        navigator.showGameDetail(id)
    }

    fun composer(id: Long) {
        navigator.showComposerDetail(id)
    }
}
