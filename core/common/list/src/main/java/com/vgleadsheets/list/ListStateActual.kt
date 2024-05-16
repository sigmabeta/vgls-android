package com.vgleadsheets.list

import com.vgleadsheets.components.ListModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ListStateActual(
    val title: String? = null,
    val listItems: ImmutableList<ListModel> = emptyList<ListModel>().toImmutableList()
)
