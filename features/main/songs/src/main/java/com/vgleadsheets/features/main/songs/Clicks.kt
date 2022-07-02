package com.vgleadsheets.features.main.songs

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter
) : ListItemClicks {
    fun song(id: Long) {
        router.showSongViewer(id)
    }
}
