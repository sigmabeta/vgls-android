package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.model.Game
import com.vgleadsheets.model.IgdbImages
import com.vgleadsheets.strings.VglsStringId
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.GridImageListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.LoadingItemListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.list.ColumnType
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.list.PaginationType
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider

@Suppress("MagicNumber")
data class State(
    val games: LCE<List<Game>> = LCE.Uninitialized,
    // Paging window: [windowStart, windowStart + games.size) within the name-ordered catalog.
    val windowStart: Int = 0,
    val hasMoreBefore: Boolean = false,
    val hasMoreAfter: Boolean = true,
    val loadingPrevious: Boolean = false,
    val loadingMore: Boolean = false,
) : ListState() {
    override val columnType = ColumnType.Regular(160)

    override val paginationType = PaginationType.Standard()

    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(VglsStringId.SCREEN_TITLE_BROWSE_GAMES)
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> {
        val content = games.withStandardErrorAndLoading(
            loadingType = LoadingType.COVER,
            loadingWithHeader = false,
        ) {
            content(data)
        }
        // Inline header/footer spinners while a page is loading above or below the window.
        return pageLoader(loadingPrevious, LOAD_PREVIOUS_OP) + content + pageLoader(loadingMore, LOAD_MORE_OP)
    }

    private fun pageLoader(show: Boolean, operationName: String): List<ListModel> {
        if (!show) return emptyList()
        val type = paginationType as? PaginationType.Paginating ?: return emptyList()
        return listOf(
            LoadingItemListModel(
                loadingType = type.loadingType,
                loadOperationName = operationName,
                loadPositionOffset = 0,
            )
        )
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

    private companion object {
        const val LOAD_PREVIOUS_OP = "games.list.load_previous"
        const val LOAD_MORE_OP = "games.list.load_more"
    }
}
