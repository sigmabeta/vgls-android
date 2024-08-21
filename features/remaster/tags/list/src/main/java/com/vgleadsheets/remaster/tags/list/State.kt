package com.vgleadsheets.remaster.tags.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingTextListtModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class State(
    val tagKeys: LCE<List<TagKey>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_TAGS)
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return when (tagKeys) {
            is LCE.Content -> content(tagKeys.data, stringProvider)
            is LCE.Error -> error(tagKeys.operationName, tagKeys.error)
            is LCE.Loading -> loading(tagKeys.operationName)
            LCE.Uninitialized -> persistentListOf()
        }.toImmutableList()
    }

    @Suppress("MagicNumber")
    private fun loading(operationName: String) = List(20) { index ->
        LoadingTextListtModel(
            withImage = true,
            withCaption = true,
            loadOperationName = operationName,
            loadPositionOffset = index
        )
    }

    private fun content(tagKeys: List<TagKey>, stringProvider: StringProvider) = tagKeys
        .map { tagKey ->
            NameCaptionListModel(
                dataId = tagKey.id,
                name = tagKey.name,
                caption = tagKey.captionText(stringProvider),
                clickAction = Action.TagKeyClicked(tagKey.id),
            )
        }

    private fun error(operationName: String, error: Throwable) = listOf(
        ErrorStateListModel(
            failedOperationName = operationName,
            errorString = error.message ?: "Unknown error."
        )
    )

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
