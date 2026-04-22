package com.vgleadsheets.remaster.offline

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val whatever: String = "",
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_OFFLINE),
        shouldShowBack = true
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> {
        TODO("Not yet implemented")
    }
}
