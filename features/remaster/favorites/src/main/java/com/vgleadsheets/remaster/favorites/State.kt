package com.vgleadsheets.remaster.favorites

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ColumnType
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider

@Suppress("MagicNumber")
data class State(
    val favoriteSongs: LCE<List<Song>> = LCE.Uninitialized,
    val favoriteGames: LCE<List<Game>> = LCE.Uninitialized,
    val favoriteComposers: LCE<List<Composer>> = LCE.Uninitialized,
) : ListState() {
    override val columnType = ColumnType.Regular(160)

    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_FAVORITES)
    )

    override fun toListItems(stringProvider: StringProvider): List<ListModel> {
        val results = listOf(
            songsToListItems(favoriteSongs, stringProvider),
            gamesToListItems(favoriteGames, stringProvider),
            composersToListItems(favoriteComposers, stringProvider),
        ).flatten()

        if (results.isEmpty()) {
            return listOf(
                EmptyStateListModel(
                    icon = Icon.JAM_EMPTY,
                    explanation = stringProvider.getString(StringId.CTA_FAVORITES),
                    showCrossOut = false
                )
            )
        }
        return results
    }

    private fun songsToListItems(
        songs: LCE<List<Song>>,
        stringProvider: StringProvider
    ) = songs.withStandardErrorAndLoading(
        loadingType = LoadingType.TEXT_CAPTION_IMAGE,
        loadingItemCount = 2,
    ) {
        if (data.isEmpty()) {
            return@withStandardErrorAndLoading emptyList()
        }

        listOf(
            SectionHeaderListModel(stringProvider.getString(StringId.SECTION_HEADER_SEARCH_SONGS))
        ) + data.map { item ->
            ImageNameCaptionListModel(
                dataId = item.id,
                name = item.name,
                caption = item.gameName,
                sourceInfo = PdfConfigById(
                    songId = item.id,
                    isAltSelected = false,
                    pageNumber = 0,
                ),
                imagePlaceholder = Icon.DESCRIPTION,
                clickAction = Action.SongClicked(item.id),
            )
        }
    }

    private fun gamesToListItems(
        games: LCE<List<Game>>,
        stringProvider: StringProvider
    ) = games.withStandardErrorAndLoading(
        loadingType = LoadingType.SQUARE,
        loadingItemCount = 2
    ) {
        if (data.isEmpty()) {
            return@withStandardErrorAndLoading emptyList()
        }

        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_SEARCH_GAMES)
            )
        ) + data.map { game ->
            SquareItemListModel(
                dataId = game.id + ID_OFFSET_GAME,
                name = game.name,
                sourceInfo = game.photoUrl,
                imagePlaceholder = Icon.ALBUM,
                clickAction = Action.GameClicked(game.id),
            )
        }
    }

    private fun composersToListItems(
        composers: LCE<List<Composer>>,
        stringProvider: StringProvider
    ) = composers.withStandardErrorAndLoading(
        loadingType = LoadingType.SQUARE,
        loadingItemCount = 2
    ) {
        if (data.isEmpty()) {
            return@withStandardErrorAndLoading emptyList()
        }

        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_SEARCH_COMPOSERS)
            )
        ) + data.map { composer ->
            SquareItemListModel(
                dataId = composer.id + ID_OFFSET_COMPOSER,
                name = composer.name,
                sourceInfo = composer.photoUrl,
                imagePlaceholder = Icon.PERSON,
                clickAction = Action.ComposerClicked(composer.id),
            )
        }
    }

    companion object {
        private const val ID_OFFSET_GAME = 1_000L
        private const val ID_OFFSET_COMPOSER = 1_000_000L
    }
}
