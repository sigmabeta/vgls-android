package com.vgleadsheets.features.main.tagkeys

import com.vgleadsheets.features.main.list.ListItemClicks
import com.vgleadsheets.nav.Navigator

class Clicks(private val navigator: Navigator) : ListItemClicks {
    fun tagKey(id: Long) {
        navigator.showValueListForTagKey(id)
    }
}
