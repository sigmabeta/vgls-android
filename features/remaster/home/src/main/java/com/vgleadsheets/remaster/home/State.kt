package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.PdfTestListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data object State : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.APP_NAME),
        shouldShowBack = false,
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return persistentListOf(
            PdfTestListModel(
                filename = "Xenoblade Chronicles 3 - Chain Attack.pdf",
                pageNumber = 0,
                clickAction = VglsAction.Noop
            )
        )
    }
}
