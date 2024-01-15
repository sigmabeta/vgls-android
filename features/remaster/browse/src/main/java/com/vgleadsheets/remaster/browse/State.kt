package com.vgleadsheets.remaster.browse

import com.vgleadsheets.components.ListModel
import kotlinx.collections.immutable.ImmutableList

data class State(
    val items: ImmutableList<ListModel>
)
