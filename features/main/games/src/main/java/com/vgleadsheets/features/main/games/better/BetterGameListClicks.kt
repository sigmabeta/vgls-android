package com.vgleadsheets.features.main.games.better

import com.vgleadsheets.features.main.list.ListItemClicks

object BetterGameListClicks : ListItemClicks {
    fun onGameClicked(
        id: Long,
        name: String,
        viewModel: BetterGameListViewModel
    ) = viewModel.onGameClicked(id, name)
}
