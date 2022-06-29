package com.vgleadsheets.features.main.composers

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

internal class Clicks(
    private val router: FragmentRouter,
    /*private val tracker: Tracker,
    private val trackingDetails: String*/
) : ListItemClicks {
    fun composer(
        id: Long,
        name: String,
    ) {
/*        tracker.logComposerClick(id, trackingDetails)*/

        router.showSongListForComposer(id)
    }
}
