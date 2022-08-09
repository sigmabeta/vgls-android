package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.NullableStringArgs
import com.vgleadsheets.features.main.list.BetterCompositeState

data class SearchState(
    val initialQuery: String? = null,
    val query: String? = null,
    override val contentLoad: SearchContent = SearchContent(
        Uninitialized,
        Uninitialized,
        Uninitialized
    ),
) : BetterCompositeState<SearchContent> {
    constructor(args: NullableStringArgs) : this(args.input)
}
