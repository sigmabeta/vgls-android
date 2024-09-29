package com.vgleadsheets.list

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleBarModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class ListStateActual(
    val columnType: ColumnType = ColumnType.One,
    val title: TitleBarModel = TitleBarModel(),
    val listItems: ImmutableList<ListModel> = emptyList<ListModel>().toImmutableList()
)
