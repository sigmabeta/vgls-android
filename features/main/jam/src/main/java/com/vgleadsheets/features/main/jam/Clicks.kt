package com.vgleadsheets.features.main.jam

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
    private val hudViewModel: HudViewModel,
    private val viewModel: JamViewModel
) : ListItemClicks {
    fun follow(jamId: Long) {
        hudViewModel.followJam(jamId)
        router.showSongViewer(null)
    }

    fun unfollow() {
        hudViewModel.unfollowJam()
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
