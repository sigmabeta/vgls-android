package com.vgleadsheets.remaster.tags.list

import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.NameCaptionListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ListState
import com.vgleadsheets.model.tag.TagKey
import net.sigmabeta.sage.ui.StringId
import net.sigmabeta.sage.ui.StringProvider

data class State(
    val tagKeys: LCE<List<TagKey>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_TAGS)
    )

    @Suppress("MagicNumber")
    override fun toListItems(stringProvider: StringProvider) = tagKeys.withStandardErrorAndLoading(
        loadingType = LoadingType.TEXT_CAPTION,
        loadingWithHeader = false
    ) {
        data.map { tagKey ->
            NameCaptionListModel(
                dataId = tagKey.id,
                name = tagKey.name,
                caption = tagKey.captionText(stringProvider),
                clickAction = Action.TagKeyClicked(tagKey.id),
            )
        }
    }

    @Suppress("LoopWithTooManyJumpStatements")
    private fun TagKey.captionText(stringProvider: StringProvider): String {
        val items = values // To avoid repetitive nullchecking
        if (items.isNullOrEmpty()) return "Error: no values found."

        val builder = StringBuilder()
        var numberOfOthers = items.size

        while (builder.length < MAX_LENGTH_SUBTITLE_CHARS) {
            val index = items.size - numberOfOthers

            if (index >= MAX_LENGTH_SUBTITLE_ITEMS) {
                break
            }

            if (numberOfOthers == 0) {
                break
            }

            if (index != 0) {
                builder.append(stringProvider.getString(StringId.TAG_CAPTION_SEPARATOR))
            }

            val stringToAppend = items[index].name
            builder.append(stringToAppend)
            numberOfOthers--
        }

        if (numberOfOthers != 0) {
            builder.append(
                stringProvider.getStringOneInt(StringId.TAG_CAPTION_AND_OTHERS, numberOfOthers)
            )
        }

        return builder.toString()
    }

    companion object {
        private const val MAX_LENGTH_SUBTITLE_CHARS = 20
        private const val MAX_LENGTH_SUBTITLE_ITEMS = 6
    }
}
