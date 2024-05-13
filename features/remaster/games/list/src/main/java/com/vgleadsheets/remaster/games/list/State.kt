package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.list.ListAction
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Game
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

data class State(
    val games: List<Game> = emptyList()
) : ListState() {
    override val renderAsGrid = true
    override fun title() = "Games"

    override fun toListItems(stringProvider: StringProvider, actionHandler: (ListAction) -> Unit): ImmutableList<ListModel> {
        return games
            .map { game ->
                SquareItemListModel(
                    dataId = game.id,
                    name = game.name,
                    imageUrl = game.photoUrl,
                    imagePlaceholder = Icon.ALBUM,
                    onClick = { actionHandler(Action.GameClicked(game.id)) }
                )
            }
            .toPersistentList()
    }
}
