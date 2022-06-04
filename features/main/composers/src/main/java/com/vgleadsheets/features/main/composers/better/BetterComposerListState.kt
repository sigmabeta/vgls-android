package com.vgleadsheets.features.main.composers.better

import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterComposerListState(
    override val contentLoad: BetterComposerListContent = BetterComposerListContent(),
) : BetterCompositeState<BetterComposerListContent>
