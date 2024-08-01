package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.components.ListModel
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
    val games: List<Game> = emptyList()
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_GAMES)
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return games
            .map { game ->
                SquareItemListModel(
                    dataId = game.id,
                    name = game.name,
                    sourceInfo = game.photoUrl,
                    imagePlaceholder = Icon.ALBUM,
                    clickAction = Action.GameClicked(game.id),
                )
            }
            .toImmutableList()
    }
}
