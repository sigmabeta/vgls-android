package com.vgleadsheets.features.main.composer

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.tracking.Tracker
import com.vgleadsheets.tracking.TrackingScreen

internal class Clicks(
    private val router: FragmentRouter,
    private val tracker: Tracker,
    private val trackingDetails: String
) : ListItemClicks {
    fun song(
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
