package com.vgleadsheets.remaster.updates

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.updates.AppUpdate
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val updates: LCE<List<AppUpdate>> = LCE.Uninitialized
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_SETTINGS),
        shouldShowBack = true
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> = listOfNotNull(
    )
}
