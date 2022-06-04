package com.vgleadsheets.features.main.composers.better

import com.vgleadsheets.features.main.list.ListItemClicks

object BetterComposerListClicks : ListItemClicks {
    fun onComposerClicked(
        id: Long,
        name: String,
        viewModel: BetterComposerListViewModel
    ) = viewModel.onComposerClicked(id, name)
}
