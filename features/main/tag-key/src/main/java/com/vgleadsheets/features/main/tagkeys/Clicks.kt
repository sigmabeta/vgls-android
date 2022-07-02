package com.vgleadsheets.features.main.tagkeys

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(private val router: FragmentRouter) : ListItemClicks {
    fun tagKey(id: Long) {
        router.showValueListForTagKey(id)
    }
}
