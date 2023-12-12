package com.vgleadsheets.features.main.tagsongs

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.Navigator

class Clicks(val navigator: Navigator) : ListItemClicks {
    fun song(id: Long) {
        navigator.showSongViewer(id)
    }
}
