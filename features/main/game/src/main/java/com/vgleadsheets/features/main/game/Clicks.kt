package com.vgleadsheets.features.main.game

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
//    private val tracker: Tracker
) : ListItemClicks {
    fun song(id: Long) {
//        tracker.logSongClick(id)
        router.showSongViewer(id)
    }
}
