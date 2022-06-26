package com.vgleadsheets.features.main.composers

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen

internal class Clicks(
    private val router: FragmentRouter,
    private val tracker: Tracker,
    private val trackingDetails: String
) : ListItemClicks {
    fun onComposerClicked(
        id: Long,
        name: String,
    ) {
        tracker.logComposerView(
            name,
            TrackingScreen.LIST_COMPOSER,
            trackingDetails
        )

        router.showSongListForComposer(id)
    }
}
