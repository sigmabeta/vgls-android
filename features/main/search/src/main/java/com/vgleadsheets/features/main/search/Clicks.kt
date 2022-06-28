package com.vgleadsheets.features.main.search

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter
) : ListItemClicks {
    fun song(id: Long) {
        router.showSongViewer(id)
    }

    fun game(id: Long) {
        router.showSongListForGame(id)
    }

    fun composer(id: Long) {
        router.showSongListForComposer(id)
    }
}
