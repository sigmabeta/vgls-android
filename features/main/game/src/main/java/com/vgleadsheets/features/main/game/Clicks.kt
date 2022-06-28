package com.vgleadsheets.features.main.game

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    val router: FragmentRouter
) : ListItemClicks {
    fun song(id: Long) {
        router.showSongViewer(id)
    }
}
