package com.vgleadsheets.features.main.composer

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
    private val hudViewModel: HudViewModel,
/*    private val tracker: Tracker,
    private val trackingDetails: String*/
) : ListItemClicks {
    fun menu() {
        hudViewModel.toMenu()
    }

    fun song(
        id: Long
    ) {
//        tracker.logSongClick(id, trackingDetails)

        router.showSongViewer(
            id
        )
    }
}
