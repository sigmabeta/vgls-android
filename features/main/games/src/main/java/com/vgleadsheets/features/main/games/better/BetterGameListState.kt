package com.vgleadsheets.features.main.games.better

import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterGameListState(
    override val contentLoad: BetterGameListContent = BetterGameListContent(),
) : BetterCompositeState<BetterGameListContent>
