package com.vgleadsheets.features.main.tagvalues

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
) : ListItemClicks {
    fun tagValue(id: Long) {
        router.showSongListForTagValue(id)
    }
}
