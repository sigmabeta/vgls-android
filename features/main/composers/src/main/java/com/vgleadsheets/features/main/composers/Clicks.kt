package com.vgleadsheets.features.main.composers

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.nav.Navigator

class Clicks(
    private val navigator: Navigator,
    private val navViewModel: NavViewModel
    /*private val tracker: Tracker,
    private val trackingDetails: String*/
) : ListItemClicks {
    fun menu() {
        navViewModel
            .toMenu()
    }

    fun composer(
        id: Long,
    ) {
/*        tracker.logComposerClick(id, trackingDetails)*/

        navigator.showComposerDetail(id)
    }
}
