package com.vgleadsheets.remaster.difficulty.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val difficultyTypes: LCE<List<TagKey>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_TAGS)
    )

    @Suppress("MagicNumber")
    override fun toListItems(stringProvider: StringProvider) = difficultyTypes.withStandardErrorAndLoading(
        loadingType = LoadingType.SINGLE_TEXT,
        loadingWithHeader = false
    ) {
        data.map { tagKey ->
            SingleTextListModel(
                dataId = tagKey.id,
                name = tagKey.name,
                clickAction = Action.DifficultyTypeClicked(tagKey.id),
            )
        }
    }
}
