package com.vgleadsheets.features.main.search.better

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.BetterCompositeState

data class BetterSearchState(
    val query: String? = null,
    val showStickerBr: Boolean = false,
    override val contentLoad: BetterSearchContent = BetterSearchContent(
        Uninitialized,
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<BetterSearchContent>
