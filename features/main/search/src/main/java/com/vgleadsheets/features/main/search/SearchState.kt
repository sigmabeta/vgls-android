package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.list.BetterCompositeState

data class SearchState(
    val query: String? = null,
    val showStickerBr: Boolean = false,
    override val contentLoad: SearchContent = SearchContent(
        Uninitialized,
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<SearchContent>
