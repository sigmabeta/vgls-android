package com.vgleadsheets.ui.viewer

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleBarModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ViewerState(
    val title: TitleBarModel = TitleBarModel(),
    val listItems: ImmutableList<ListModel> = emptyList<ListModel>().toImmutableList(),
    val initialPage: Int = 0,
)
