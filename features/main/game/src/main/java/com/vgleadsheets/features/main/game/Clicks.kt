package com.vgleadsheets.features.main.game

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.Navigator

class Clicks(
    private val navigator: Navigator,
    private val viewModel: GameDetailViewModel,
) : ListItemClicks {
    fun song(id: Long) {
        navigator.showSongViewer(id)
    }

    fun composer(id: Long) {
        navigator.showComposerDetail(id)
    }

    fun onFavoriteClick() {
        viewModel.onFavoriteClick()
    }
}
