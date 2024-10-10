package com.vgleadsheets.remaster.parts

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Part
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val selectedPart: Part? = null
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_PART_SELECTOR),
        shouldShowBack = true
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> = Part
        .entries
        .map { PartSelectorOption.valueOf(it.name) }
        .map {
            MenuItemListModel(
                name = stringProvider.getString(it.longResId),
                caption = null,
                icon = Icon.DESCRIPTION,
                clickAction = Action.PartSelected(it),
                selected = it.apiId == selectedPart?.apiId
            )
        }
}
