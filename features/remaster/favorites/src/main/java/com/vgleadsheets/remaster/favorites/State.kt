package com.vgleadsheets.remaster.favorites

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class State(
    val favoriteSongs: LCE<List<Song>> = LCE.Uninitialized,
    val favoriteGames: LCE<List<Game>> = LCE.Uninitialized,
    val favoriteComposers: LCE<List<Composer>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel(
        title = stringProvider.getString(StringId.SCREEN_TITLE_BROWSE_FAVORITES)
    )

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        return listOf(
            songsToListItems(favoriteSongs, stringProvider),
            gamesToListItems(favoriteGames, stringProvider),
            composersToListItems(favoriteComposers, stringProvider),
        )
            .flatten()
            .toImmutableList()
    }

    private fun songsToListItems(songs: LCE<List<Song>>, stringProvider: StringProvider): List<ListModel> {
        return when (songs) {
            is LCE.Content -> content(songs.data, stringProvider)
            is LCE.Error -> error(songs.operationName, songs.error)
            is LCE.Loading -> loading(songs.operationName, LoadingType.TEXT_CAPTION_IMAGE, 2, true)
            LCE.Uninitialized -> persistentListOf()
        }
    }

    private fun gamesToListItems(games: LCE<List<Game>>, stringProvider: StringProvider): List<ListModel> {
        return when (games) {
            is LCE.Content -> content(games.data, stringProvider)
            is LCE.Error -> error(games.operationName, games.error)
            is LCE.Loading -> loading(games.operationName, LoadingType.SQUARE, 2, true)
            LCE.Uninitialized -> persistentListOf()
        }
    }

    private fun composersToListItems(composers: LCE<List<Composer>>, stringProvider: StringProvider): List<ListModel> {
        return when (composers) {
            is LCE.Content -> content(composers.data, stringProvider)
            is LCE.Error -> error(composers.operationName, composers.error)
            is LCE.Loading -> loading(composers.operationName, LoadingType.SQUARE, 2, true)
            LCE.Uninitialized -> persistentListOf()
        }
    }

    private fun <ItemType> content(data: List<ItemType>, stringProvider: StringProvider): List<ListModel> {
        if (data.isEmpty()) {
            return emptyList()
        }

        return when (data.first()) {
            is Song -> songsToContentListItems(data as List<Song>, stringProvider)
            is Game -> gamesToContentListItems(data as List<Game>, stringProvider)
            is Composer -> composersToContentListItems(data as List<Composer>, stringProvider)
            else -> throw IllegalArgumentException("Invalid type.")
        }
    }

    private fun songsToContentListItems(songs: List<Song>, stringProvider: StringProvider) = listOf(
        SectionHeaderListModel(stringProvider.getString(StringId.SECTION_HEADER_SEARCH_SONGS))
    ) + songs.map { item ->
        ImageNameCaptionListModel(
            dataId = item.id,
            name = item.name,
            caption = item.gameName,
            sourceInfo = PdfConfigById(
                songId = item.id,
                pageNumber = 0
            ),
            imagePlaceholder = Icon.DESCRIPTION,
            clickAction = Action.SongClicked(item.id),
        )
    }

    private fun gamesToContentListItems(games: List<Game>, stringProvider: StringProvider) = listOf(
        SectionHeaderListModel(
            stringProvider.getString(StringId.SECTION_HEADER_SEARCH_GAMES)
        )
    ) + games.map { game ->
        SquareItemListModel(
            dataId = game.id + ID_OFFSET_GAME,
            name = game.name,
            sourceInfo = game.photoUrl,
            imagePlaceholder = Icon.ALBUM,
            clickAction = Action.GameClicked(game.id),
        )
    }

    private fun composersToContentListItems(composers: List<Composer>, stringProvider: StringProvider) = listOf(
        SectionHeaderListModel(
            stringProvider.getString(StringId.SECTION_HEADER_SEARCH_COMPOSERS)
        )
    ) + composers.map { composer ->
        SquareItemListModel(
            dataId = composer.id + ID_OFFSET_COMPOSER,
            name = composer.name,
            sourceInfo = composer.photoUrl,
            imagePlaceholder = Icon.PERSON,
            clickAction = Action.ComposerClicked(composer.id),
        )
    }

    companion object {
        private const val ID_OFFSET_GAME = 1_000L
        private const val ID_OFFSET_COMPOSER = 1_000_000L
    }
}
