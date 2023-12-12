package com.vgleadsheets.features.main.composer

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.Navigator

class Clicks(
    private val navigator: Navigator,
    private val viewModel: ComposerDetailViewModel,
) : ListItemClicks {
    fun song(
        id: Long
    ) {
        navigator.showSongViewer(
            id
        )
    }

    fun onFavoriteClick() {
        viewModel.onFavoriteClick()
    }

    fun game(id: Long) {
        navigator.showGameDetail(id)
    }
}
