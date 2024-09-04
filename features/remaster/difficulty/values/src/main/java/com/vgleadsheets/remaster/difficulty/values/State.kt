package com.vgleadsheets.remaster.difficulty.values

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LabelRatingStarListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val difficultyType: LCE<TagKey> = LCE.Uninitialized,
    val difficultyValues: LCE<List<TagValue>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = if (difficultyType is LCE.Content) {
            stringProvider.getStringOneArg(StringId.SCREEN_TITLE_BROWSE_BY_TAG, difficultyType.data.name)
        } else {
            stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_TAGS)
        }
    )

    @Suppress("MagicNumber")
    override fun toListItems(stringProvider: StringProvider) = difficultyValues.withStandardErrorAndLoading(
        loadingType = LoadingType.SINGLE_TEXT,
        loadingWithHeader = false,
    ) {
        data.map { difficultyValue ->
            val valueAsInt = difficultyValue.name.toIntOrNull() ?: -1
            val label = valueAsInt.getLabel(stringProvider)
            LabelRatingStarListModel(
                dataId = difficultyValue.id,
                label = label,
                value = valueAsInt,
                clickAction = Action.DifficultyValueClicked(difficultyValue.id),
            )
        }
    }
}

@Suppress("MagicNumber")
private fun Int.getLabel(stringProvider: StringProvider): String {
    val stringId = when (this) {
        1 -> StringId.DIFFICULTY_ONE
        2 -> StringId.DIFFICULTY_TWO
        3 -> StringId.DIFFICULTY_THREE
        4 -> StringId.DIFFICULTY_FOUR
        else -> throw IllegalArgumentException("Badly formatted difficulty value.")
    }

    return stringProvider.getString(stringId)
}
