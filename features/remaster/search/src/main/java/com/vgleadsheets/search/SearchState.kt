package com.vgleadsheets.bottombar

import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.SearchHistoryListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SearchHistoryEntry
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.search.Action
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SearchState(
    val showHistory: Boolean = true,
    val searchHistory: List<SearchHistoryEntry> = emptyList(),
    val songResults: List<Song> = emptyList(),
    val gameResults: List<Game> = emptyList(),
    val composerResults: List<Composer> = emptyList(),
    val sheetUrlInfo: UrlInfo = UrlInfo(),
) : VglsState {
    fun resultItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        if (showHistory) {
            return historyItems()
        }
        return (songItems(stringProvider) + gameItems(stringProvider) + composerItems(stringProvider))
            .toImmutableList()
    }

    private fun historyItems(): ImmutableList<ListModel> = searchHistory
        .map { entry ->
            SearchHistoryListModel(
                dataId = entry.id!!,
                name = entry.query,
                clickAction = Action.SearchHistoryEntryClicked(entry.query),
                removeAction = Action.SearchHistoryEntryRemoveClicked(entry.id!!),
            )
        }.toImmutableList()

    private fun songItems(stringProvider: StringProvider) = if (songResults.isNotEmpty()) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_SEARCH_SONGS)
            )
        ) + songResults.map { song ->
            ImageNameCaptionListModel(
                dataId = song.id,
                name = song.name,
                caption = song.gameName,
                sourceInfo = PdfConfigById(
                    songId = song.id,
                    pageNumber = 0
                ),
                imagePlaceholder = Icon.DESCRIPTION,
                clickAction = Action.SongClicked(song.id),
            )
        }
    } else {
        emptyList()
    }

    private fun gameItems(stringProvider: StringProvider) = if (gameResults.isNotEmpty()) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_SEARCH_GAMES)
            )
        ) + gameResults.map { game ->
            SquareItemListModel(
                dataId = game.id + ID_OFFSET_GAME,
                name = game.name,
                sourceInfo = game.photoUrl,
                imagePlaceholder = Icon.PERSON,
                clickAction = Action.GameClicked(game.id),
            )
        }
    } else {
        emptyList()
    }

    private fun composerItems(stringProvider: StringProvider) = if (composerResults.isNotEmpty()) {
        listOf(
            SectionHeaderListModel(
                stringProvider.getString(StringId.SECTION_HEADER_SEARCH_COMPOSERS)
            )
        ) + composerResults.map { composer ->
            SquareItemListModel(
                dataId = composer.id + ID_OFFSET_COMPOSER,
                name = composer.name,
                sourceInfo = composer.photoUrl,
                imagePlaceholder = Icon.PERSON,
                clickAction = Action.ComposerClicked(composer.id),
            )
        }
    } else {
        emptyList()
    }

    companion object {
        private const val ID_OFFSET_GAME = 1_000L
        private const val ID_OFFSET_COMPOSER = 1_000_000L
    }
}
