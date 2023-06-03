package com.vgleadsheets.features.main.game

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
    private val viewModel: GameViewModel,
) : ListItemClicks {
    fun song(id: Long) {
        router.showSongViewer(id)
    }

    fun onFavoriteClick() {
        viewModel.onFavoriteClick()
    }
}
