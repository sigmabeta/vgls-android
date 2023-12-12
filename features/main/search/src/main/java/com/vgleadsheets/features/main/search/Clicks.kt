package com.vgleadsheets.features.main.search

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.nav.Navigator

class Clicks(
    private val navigator: Navigator,
    private val navViewModel: NavViewModel
) : ListItemClicks {
    fun song(id: Long) {
        navViewModel
            .hideSearch()
        navigator.showSongViewer(id)
    }

    fun game(id: Long) {
        navViewModel
            .hideSearch()
        navigator.showGameDetail(id)
    }

    fun composer(id: Long) {
        navViewModel
            .hideSearch()
        navigator.showComposerDetail(id)
    }
}
