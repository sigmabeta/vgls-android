package com.vgleadsheets.remaster.tags.values

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val tagKey: LCE<TagKey> = LCE.Uninitialized,
    val tagValues: LCE<List<TagValue>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = if (tagKey is LCE.Content) {
            stringProvider.getStringOneArg(StringId.SCREEN_TITLE_BROWSE_BY_TAG, tagKey.data.name)
        } else {
            stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_TAGS)
        }
    )

    @Suppress("MagicNumber")
    override fun toListItems(stringProvider: StringProvider) = tagValues.withStandardErrorAndLoading(
        loadingType = LoadingType.SINGLE_TEXT,
        loadingWithHeader = false,
    ) {
        data.map { tagValue ->
            SingleTextListModel(
                dataId = tagValue.id,
                name = tagValue.name,
                clickAction = Action.TagValueClicked(tagValue.id),
            )
        }
    }
}
