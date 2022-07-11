package com.vgleadsheets.features.main.composers

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
    private val hudViewModel: HudViewModel
    /*private val tracker: Tracker,
    private val trackingDetails: String*/
) : ListItemClicks {
    fun menu() {
        hudViewModel.toMenu()
    }

    fun composer(
        id: Long,
    ) {
/*        tracker.logComposerClick(id, trackingDetails)*/

        router.showSongListForComposer(id)
    }
}
