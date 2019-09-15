package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.search.SearchResult

data class SearchState(
    val query: String? = null,
    val results: Async<List<SearchResult>> = Uninitialized
) : MvRxState
