package com.vgleadsheets.features.main.composer

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen

internal class Clicks(
    val viewModel: ComposerDetailViewModel,
    val router: FragmentRouter,
    val tracker: Tracker,
    val trackingDetails: String
) : ListItemClicks {
    fun onSongClicked(
        id: Long,
        name: String,
        gameName: String,
        apiId: String
    ) {
        tracker.logSongView(
            id,
            name,
            gameName,
            apiId,
            TrackingScreen.DETAIL_COMPOSER,
            trackingDetails
        )

        router.showSongViewer(
            id
        )
    }
}
