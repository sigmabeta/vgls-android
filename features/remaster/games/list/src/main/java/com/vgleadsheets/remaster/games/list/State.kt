package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.components.ListModel
import kotlinx.collections.immutable.ImmutableList

data class State(
    val listItems: ImmutableList<ListModel>
)
