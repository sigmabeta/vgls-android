package com.vgleadsheets.remaster.tags.values

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

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

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return when (tagValues) {
            is LCE.Content -> content(tagValues.data)
            is LCE.Error -> error(tagValues.operationName, tagValues.error)
            is LCE.Loading -> loading(tagValues.operationName, loadingType = LoadingType.TEXT_IMAGE, 20)
            LCE.Uninitialized -> persistentListOf()
        }.toImmutableList()
    }

    private fun content(tagValues: List<TagValue>) = tagValues
        .map { tagValue ->
            SingleTextListModel(
                dataId = tagValue.id,
                name = tagValue.name,
                clickAction = Action.TagValueClicked(tagValue.id),
            )
        }
}
