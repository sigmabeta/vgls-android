package com.vgleadsheets.features.main.composer

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
    private val viewModel: ComposerDetailViewModel,
) : ListItemClicks {
    fun song(
        id: Long
    ) {
        router.showSongViewer(
            id
        )
    }

    fun onFavoriteClick() {
        viewModel.onFavoriteClick()
    }

    fun game(id: Long) {
        router.showGameDetail(id)
    }
}
