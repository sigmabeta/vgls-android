package com.vgleadsheets.remaster.home

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.state.VglsAction
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data object State : ListState() {
    override fun title() = null

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return persistentListOf(
            NameCaptionListModel(
                1234L,
                "Home Screen",
                "To Be Continued",
                clickAction = VglsAction.Resume
            )
        )
    }
}
