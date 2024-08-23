package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingItemListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Game
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class State(
    val games: LCE<List<Game>> = LCE.Uninitialized
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_GAMES)
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return when (games) {
            is LCE.Content -> content(games.data)
            is LCE.Error -> error(games.operationName, games.error)
            is LCE.Loading -> loading(games.operationName)
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

    private fun content(games: List<Game>) = games
        .map { game ->
            SquareItemListModel(
                dataId = game.id,
                name = game.name,
                sourceInfo = game.photoUrl,
                imagePlaceholder = Icon.ALBUM,
                clickAction = Action.GameClicked(game.id),
            )
        }
}
