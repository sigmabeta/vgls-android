package com.vgleadsheets.list

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleBarModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ListStateActual(
    val title: TitleBarModel = TitleBarModel(),
    val listItems: ImmutableList<ListModel> = emptyList<ListModel>().toImmutableList()
)
