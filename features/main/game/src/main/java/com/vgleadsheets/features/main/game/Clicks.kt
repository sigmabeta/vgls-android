package com.vgleadsheets.features.main.game

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
    private val viewModel: GameDetailViewModel,
) : ListItemClicks {
    fun song(id: Long) {
        router.showSongViewer(id)
    }

    fun composer(id: Long) {
        router.showComposerDetail(id)
    }

    fun onFavoriteClick() {
        viewModel.onFavoriteClick()
    }
}
