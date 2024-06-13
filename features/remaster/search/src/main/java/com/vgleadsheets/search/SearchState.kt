package com.vgleadsheets.bottombar

import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.components.ListModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SearchState(
    val resultItems: ImmutableList<ListModel> = emptyList<ListModel>().toImmutableList(),
) : VglsState
