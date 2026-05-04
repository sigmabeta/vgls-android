package com.vgleadsheets.remaster.parts

import com.vgleadsheets.model.Part
import com.vgleadsheets.strings.StringId
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.MenuItemListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider

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
