package com.vgleadsheets.features.main.games

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter
) : ListItemClicks {
    fun game(id: Long) {
        router.showGameDetail(id)
    }
}
