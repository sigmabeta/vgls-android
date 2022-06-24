package com.vgleadsheets.features.main.composers

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen

internal class Clicks(
    val viewModel: ComposerListViewModel,
    val router: FragmentRouter,
    val tracker: Tracker,
    val trackingDetails: String
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
