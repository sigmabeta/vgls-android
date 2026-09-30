package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.model.Game
import com.vgleadsheets.model.IgdbImages
import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.GridImageListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ColumnType
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider

@Suppress("MagicNumber")
data class State(
    val games: LCE<List<Game>> = LCE.Uninitialized
) : ListState() {
    override val columnType = ColumnType.Regular(160)

    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(VglsStringId.SCREEN_TITLE_BROWSE_GAMES)
    )

    override fun toListItems(stringProvider: StringProvider) = games.withStandardErrorAndLoading(
        loadingType = LoadingType.COVER,
        loadingWithHeader = false,
    ) {
        content(data)
    }

    private fun content(games: List<Game>) = games
        .map { game ->
            GridImageListModel(
                dataId = game.id,
                name = game.name,
                sourceInfo = game.imageUrl,
                aspectRatio = IgdbImages.COVER_ASPECT_RATIO,
                imagePlaceholder = Icon.Album,
                clickAction = Action.GameClicked(game.id),
            )
        }
}
