package com.vgleadsheets.features.main.games

import com.vgleadsheets.features.main.games.better.BetterGameListViewModel
import com.vgleadsheets.features.main.list.ListItemClicks

object GameListClicks : ListItemClicks {
    fun onGameClicked(
        id: Long,
        name: String,
        viewModel: BetterGameListViewModel
    ) = viewModel.onGameClicked(id, name)
}
