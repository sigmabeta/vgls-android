package com.vgleadsheets.features.main.jams.better

import com.vgleadsheets.features.main.list.ListItemClicks

object BetterJamListClicks : ListItemClicks {
    fun onJamClicked(
        id: Long,
        name: String,
        viewModel: BetterJamListViewModel
    ) = viewModel.onJamClicked(id, name)

    fun onFindJamClicked(viewModel: BetterJamListViewModel) = viewModel.onFindJamClicked()
}
