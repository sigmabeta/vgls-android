package com.vgleadsheets.features.main.songs

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.Navigator

class Clicks(
    private val navigator: Navigator
) : ListItemClicks {
    fun song(id: Long) {
        navigator.showSongViewer(id)
    }
}
