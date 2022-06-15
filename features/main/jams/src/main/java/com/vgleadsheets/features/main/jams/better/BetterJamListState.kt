package com.vgleadsheets.features.main.jams.better

import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterJamListState(
    override val contentLoad: BetterJamListContent = BetterJamListContent(),
) : BetterCompositeState<BetterJamListContent>
