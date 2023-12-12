package com.vgleadsheets.features.main.tagvalues

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.Navigator

class Clicks(
    private val navigator: Navigator,
) : ListItemClicks {
    fun tagValue(id: Long) {
        navigator.showSongListForTagValue(id)
    }
}
