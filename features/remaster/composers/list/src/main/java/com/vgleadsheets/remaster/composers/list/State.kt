package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingItemListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val composers: LCE<List<Composer>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_COMPOSERS)
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return when (composers) {
            is LCE.Content -> content(composers.data)
            is LCE.Error -> error(composers.operationName, composers.error)
            is LCE.Loading -> loading(composers.operationName)
            LCE.Uninitialized -> emptyList()
        }.toImmutableList()
    }

    @Suppress("MagicNumber")
    private fun loading(operationName: String) = List(20) { index ->
        LoadingItemListModel(
            loadingType = LoadingType.SQUARE,
            loadOperationName = operationName,
            loadPositionOffset = index
        )
    }

    private fun content(composers: List<Composer>) = composers
        .map { composer ->
            SquareItemListModel(
                dataId = composer.id,
                name = composer.name,
                sourceInfo = composer.photoUrl,
                imagePlaceholder = Icon.PERSON,
                clickAction = Action.ComposerClicked(composer.id),
            )
        }

    private fun error(operationName: String, error: Throwable) = listOf(
        ErrorStateListModel(
            failedOperationName = operationName,
            errorString = error.message ?: "Unknown error."
        )
    )
}
