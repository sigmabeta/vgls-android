package com.vgleadsheets.remaster.tags.values

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val tagKey: TagKey? = null,
    val tagValues: List<TagValue> = emptyList(),
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = if (tagKey != null) {
            stringProvider.getStringOneArg(StringId.SCREEN_TITLE_BROWSE_BY_TAG, tagKey.name)
        } else {
            stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_TAGS)
        }
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return tagValues
            .map { tagKey ->
                SingleTextListModel(
                    dataId = tagKey.id,
                    name = tagKey.name,
                    clickAction = Action.TagValueClicked(tagKey.id),
                )
            }
            .toImmutableList()
    }

    companion object {
        private const val MAX_LENGTH_SUBTITLE_CHARS = 20
        private const val MAX_LENGTH_SUBTITLE_ITEMS = 6
    }
}
