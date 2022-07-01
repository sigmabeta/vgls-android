package com.vgleadsheets.features.main.composer

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

internal class Clicks(
    private val router: FragmentRouter,
/*    private val tracker: Tracker,
    private val trackingDetails: String*/
) : ListItemClicks {
    fun song(
        id: Long
    ) {
//        tracker.logSongClick(id, trackingDetails)

        router.showSongViewer(
            id
        )
    }
}
