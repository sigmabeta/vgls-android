package com.vgleadsheets.features.main.tagkeys.better

import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterTagKeyListState(
    override val contentLoad: BetterTagKeyListContent = BetterTagKeyListContent(),
) : BetterCompositeState<BetterTagKeyListContent>
