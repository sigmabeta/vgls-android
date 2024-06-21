package com.vgleadsheets.remaster.home

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val moduleStates: List<HomeModuleState> = emptyList()
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.APP_NAME),
        shouldShowBack = false,
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return moduleStates
            .sortedBy { it.priority }
            .map { it.toListItems() }
            .flatten()
            .toImmutableList()
    }
}
