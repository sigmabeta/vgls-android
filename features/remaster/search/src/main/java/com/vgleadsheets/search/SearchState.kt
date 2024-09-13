package com.vgleadsheets.search

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.SearchHistoryListModel
import com.vgleadsheets.components.SquareItemListModel
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SearchHistoryEntry
import com.vgleadsheets.pdf.PdfConfigById
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.ui.id
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

data class SearchState(
    val searchQuery: String = "",
    val searchHistory: LCE<List<SearchHistoryEntry>> = LCE.Uninitialized,
    val songResults: LCE<List<Song>> = LCE.Uninitialized,
    val gameResults: LCE<List<Game>> = LCE.Uninitialized,
    val composerResults: LCE<List<Composer>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel()

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        if (searchQuery.isBlank()) {
            val historyItems = historyItems()

            return if (historyItems.size < 5) {
                historyItems + searchCta(stringProvider)
            } else {
                historyItems
            }.toImmutableList()
        }
        return (songItems() + gameItems() + composerItems())
            .toImmutableList()
    }

    private fun historyItems(): ImmutableList<ListModel> = searchHistory.withStandardErrorAndLoading(
        loadingType = LoadingType.SINGLE_TEXT,
        loadingItemCount = 10,
        loadingWithHeader = false
    ) {
        data.map { entry ->
            SearchHistoryListModel(
                dataId = entry.id!!,
                name = entry.query,
                clickAction = Action.SearchHistoryEntryClicked(entry.query),
                removeAction = Action.SearchHistoryEntryRemoveClicked(entry.id!!),
            )
        }
    }

    private fun searchCta(stringProvider: StringProvider) = listOf(
        EmptyStateListModel(
            iconId = Icon.SEARCH.id(),
            explanation = stringProvider.getString(StringId.CTA_SEARCH),
            showCrossOut = false,
        )
    )

    private fun songItems() = songResults.withStandardErrorAndLoading(
        loadingType = LoadingType.TEXT_CAPTION_IMAGE,
        loadingItemCount = 2,
    ) {
        data.map { song ->
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
    }

    private fun gameItems() = gameResults.withStandardErrorAndLoading(
        loadingType = LoadingType.SQUARE,
        loadingItemCount = 2,
    ) {
        data.map { game ->
            SquareItemListModel(
                dataId = game.id + ID_OFFSET_GAME,
                name = game.name,
                sourceInfo = game.photoUrl,
                imagePlaceholder = Icon.ALBUM,
                clickAction = Action.GameClicked(game.id),
            )
        }
    }

    private fun composerItems() = composerResults.withStandardErrorAndLoading(
        loadingType = LoadingType.SQUARE,
        loadingItemCount = 2,
    ) {
        data.map { composer ->
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
