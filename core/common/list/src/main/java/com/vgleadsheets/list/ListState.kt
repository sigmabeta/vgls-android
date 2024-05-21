package com.vgleadsheets.list

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.state.VglsState
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList

abstract class ListState : VglsState {
    abstract fun title(stringProvider: StringProvider): TitleBarModel
    abstract fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel>
    open val renderAsGrid = false

    fun toActual(stringProvider: StringProvider): ListStateActual {
        return ListStateActual(
            title = title(stringProvider),
            listItems = toListItems(stringProvider),
        )
    }
}
