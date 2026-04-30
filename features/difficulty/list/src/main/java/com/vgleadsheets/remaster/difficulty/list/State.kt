package com.vgleadsheets.remaster.difficulty.list

import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.SingleTextListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import com.vgleadsheets.model.tag.TagKey
import net.sigmabeta.sage.ui.StringId
import net.sigmabeta.sage.ui.StringProvider

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
