package com.vgleadsheets.features.main.jam

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
    private val viewModel: JamViewModel
) : ListItemClicks {
    fun follow(jamId: Long) {
        router.showJamViewer(jamId)
    }

    fun refresh() {
        viewModel.refreshJam()
    }

    fun delete() {
        viewModel.deleteJam()
    }

    fun song(id: Long) {
        router.showSongViewer(id)
    }
}
