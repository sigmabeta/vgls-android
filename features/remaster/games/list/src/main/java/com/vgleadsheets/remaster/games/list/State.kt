package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ColumnType
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Game
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

data class State(
    val games: LCE<List<Game>> = LCE.Uninitialized
) : ListState() {
    override val columnType = ColumnType.Regular(160)

    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_GAMES)
    )

    override fun toListItems(stringProvider: StringProvider) = games.withStandardErrorAndLoading(
        loadingType = LoadingType.SQUARE,
        loadingWithHeader = false,
    ) {
        content(data)
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
