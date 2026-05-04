package com.vgleadsheets.remaster.tags.values

import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.SingleTextListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val tagKey: LCE<TagKey> = LCE.Uninitialized,
    val tagValues: LCE<List<TagValue>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = if (tagKey is LCE.Content) {
            stringProvider.getStringOneArg(VglsStringId.SCREEN_TITLE_BROWSE_BY_TAG, tagKey.data.name)
        } else {
            stringProvider.getString(VglsStringId.SCREEN_TITLE_BROWSE_TAGS)
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
