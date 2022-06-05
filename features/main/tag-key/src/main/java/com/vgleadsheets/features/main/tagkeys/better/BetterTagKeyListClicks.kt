package com.vgleadsheets.features.main.tagkeys.better

import com.vgleadsheets.features.main.list.ListItemClicks

object BetterTagKeyListClicks : ListItemClicks {
    fun onTagKeyClicked(
        id: Long,
        name: String,
        viewModel: BetterTagKeyListViewModel
    ) = viewModel.onTagKeyClicked(id, name)
}
