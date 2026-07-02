package com.vgleadsheets.search

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.history.SearchHistoryEntry
import com.vgleadsheets.search.SearchViewModel.Companion.MINIMUM_LENGTH_QUERY
import com.vgleadsheets.strings.VglsStringId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.components.EmptyStateListModel
import net.sigmabeta.sage.components.ErrorStateListModel
import net.sigmabeta.sage.components.ImageNameCaptionListModel
import net.sigmabeta.sage.components.ListModel
import net.sigmabeta.sage.components.LoadingType
import net.sigmabeta.sage.components.NoopListModel
import net.sigmabeta.sage.components.SearchHistoryListModel
import net.sigmabeta.sage.components.SectionHeaderListModel
import net.sigmabeta.sage.components.GridImageListModel
import net.sigmabeta.sage.components.TitleBarModel
import net.sigmabeta.sage.images.PdfSize
import net.sigmabeta.sage.images.SourceInfo
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.list.checkForDupes
import net.sigmabeta.sage.pdf.PdfConfigById
import net.sigmabeta.sage.ui.Icon
import net.sigmabeta.sage.ui.StringProvider

@Suppress("MagicNumber")
data class SearchState(
    val searchQuery: String = "",
    val searchHistory: LCE<List<SearchHistoryEntry>> = LCE.Uninitialized,
    val songResults: LCE<List<Song>> = LCE.Uninitialized,
    val gameResults: LCE<List<Game>> = LCE.Uninitialized,
    val composerResults: LCE<List<Composer>> = LCE.Uninitialized,
) : ListState() {
    override fun title(stringProvider: StringProvider) = TitleBarModel()

    @Suppress("TooGenericExceptionCaught")
    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> {
        val tempList = if (searchQuery.length < MINIMUM_LENGTH_QUERY) {
            val historyItems = historyItems()

            if (historyItems.size < 5) {
                historyItems + searchCta(stringProvider)
            } else {
                historyItems
            }
        } else {
            songItems(stringProvider) + gameItems(stringProvider) + composerItems(stringProvider)
        }
            .filter { it !is NoopListModel }
            .ifEmpty {
                listOf(
                    EmptyStateListModel(
                        icon = Icon.Search,
                        explanation = stringProvider.getString(VglsStringId.CTA_SEARCH_OTHER_QUERY),
                        showCrossOut = false
                    )
                )
            }
            .toImmutableList()

        try {
            checkForDupes(tempList)
        } catch (ex: Exception) {
            val errorMessage = stringProvider.getString(VglsStringId.ERROR_BROKEN_SCREEN_DESC)

            val items = persistentListOf(
                ErrorStateListModel(
                    failedOperationName = "renderScreen",
                    errorString = errorMessage,
                    error = ex,
                )
            )

            return items.toImmutableList()
        }

        return tempList
    }

    private fun historyItems(): List<ListModel> = searchHistory.withStandardErrorAndLoading(
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
            icon = Icon.Search,
            explanation = stringProvider.getString(VglsStringId.CTA_SEARCH),
            showCrossOut = false,
        )
    )

    private fun songItems(stringProvider: StringProvider) = songResults.withStandardErrorAndLoading(
        loadingType = LoadingType.TEXT_CAPTION_IMAGE,
        loadingItemCount = 2,
    ) {
        if (data.isEmpty()) {
            return@withStandardErrorAndLoading emptyList()
        }

        listOf(
            SectionHeaderListModel(
                stringProvider.getString(VglsStringId.SECTION_HEADER_SEARCH_SONGS)
            )
        ) + data.map { song ->
            ImageNameCaptionListModel(
                dataId = song.id,
                name = song.name,
                caption = song.gameName,
                sourceInfo = SourceInfo(
                    PdfConfigById(
                        songId = song.id,
                        pageNumber = 0,
                        isAltSelected = false,
                        pdfSize = PdfSize.THUMBNAIL,
                    )
                ),
                imagePlaceholder = Icon.Description,
                clickAction = Action.SongClicked(song.id),
            )
        }
    }

    private fun gameItems(stringProvider: StringProvider) = gameResults.withStandardErrorAndLoading(
        loadingType = LoadingType.SQUARE,
        loadingItemCount = 2,
    ) {
        if (data.isEmpty()) {
            return@withStandardErrorAndLoading emptyList()
        }

        listOf(
            SectionHeaderListModel(
                stringProvider.getString(VglsStringId.SECTION_HEADER_SEARCH_GAMES)
            )
        ) + data.map { game ->
            GridImageListModel(
                dataId = game.id + ID_OFFSET_GAME,
                name = game.name,
                sourceInfo = game.photoUrl,
                imagePlaceholder = Icon.Album,
                clickAction = Action.GameClicked(game.id),
            )
        }
    }

    private fun composerItems(stringProvider: StringProvider) = composerResults.withStandardErrorAndLoading(
        loadingType = LoadingType.SQUARE,
        loadingItemCount = 2,
    ) {
        if (data.isEmpty()) {
            return@withStandardErrorAndLoading emptyList()
        }

        listOf(
            SectionHeaderListModel(
                stringProvider.getString(VglsStringId.SECTION_HEADER_SEARCH_COMPOSERS)
            )
        ) + data.map { composer ->
            GridImageListModel(
                dataId = composer.id + ID_OFFSET_COMPOSER,
                name = composer.name,
                sourceInfo = composer.photoUrl,
                imagePlaceholder = Icon.Person,
                clickAction = Action.ComposerClicked(composer.id),
            )
        }
    }

    companion object {
        private const val ID_OFFSET_GAME = 1_000_000_000L
        private const val ID_OFFSET_COMPOSER = 1_000_000L
    }
}
