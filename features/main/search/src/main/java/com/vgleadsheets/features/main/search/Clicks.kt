package com.vgleadsheets.features.main.search

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
    private val hudViewModel: HudViewModel
) : ListItemClicks {
    fun song(id: Long) {
        hudViewModel.hideSearch()
        router.showSongViewer(id)
    }

    fun game(id: Long) {
        hudViewModel.hideSearch()
        router.showSongListForGame(id)
    }

    fun composer(id: Long) {
        hudViewModel.hideSearch()
        router.showSongListForComposer(id)
    }
}
