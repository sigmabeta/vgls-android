package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingImageNameCaptionListModel
import com.vgleadsheets.components.MenuSearchListModel
import com.vgleadsheets.components.SearchEmptyStateListModel
import com.vgleadsheets.components.SearchErrorStateListModel
import com.vgleadsheets.components.SearchResultListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.Clicks
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.features.main.hud.search.SearchContent
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.thumbUrl

object Search {
    fun getListModels(
        hudMode: HudMode,
        searchQuery: String?,
        selectedPart: Part,
        contentLoad: SearchContent,
        baseImageUrl: String,
        clicks: Clicks,
        onTextEntered: (String) -> Unit,
        onMenuButtonClick: () -> Unit,
        onClearClick: () -> Unit,
        resources: Resources
    ) = if (hudMode == HudMode.SEARCH) {
        searchResults(
            searchQuery,
            contentLoad,
            selectedPart,
            baseImageUrl,
            clicks,
            resources
        ) + listOf(
            MenuSearchListModel(
                searchQuery,
                onTextEntered,
                onMenuButtonClick,
                onClearClick
            )
        )
    } else {
        emptyList()
    }

    private fun searchResults(
        query: String?,
        contentLoad: SearchContent,
        selectedPart: Part,
        baseImageUrl: String,
        clicks: Clicks,
        resources: Resources
    ): List<ListModel> {

        if (query.isNullOrEmpty()) {
            return listOf(
                SearchEmptyStateListModel()
            )
        }

        if (query.startsWith("stickerbr")) {
            return listOf(
                SearchErrorStateListModel(
                    "stickerbrush",
                    resources.getString(R.string.search_error_search_stickerbrush),
                )
            )
        }

        val songModels = createSectionModels(
            R.string.search_section_header_songs,
            contentLoad.songs,
            baseImageUrl,
            selectedPart,
            clicks,
            resources
        )
        val gameModels = createSectionModels(
            R.string.search_section_header_games,
            contentLoad.games,
            baseImageUrl,
            selectedPart,
            clicks,
            resources
        )
        val composerModels = createSectionModels(
            R.string.search_section_header_composers,
            contentLoad.composers,
            baseImageUrl,
            selectedPart,
            clicks,
            resources
        )

        val listModels = songModels + gameModels + composerModels
        return listModels.ifEmpty {
            listOf(
                SearchEmptyStateListModel(
                    R.drawable.ic_description_24dp,
                    resources.getString(R.string.search_empty_search_no_results),
                )
            )
        }
    }

    private fun createSectionModels(
        sectionId: Int,
        results: Async<List<Any>>,
        baseImageUrl: String,
        selectedPart: Part,
        clicks: Clicks,
        resources: Resources
    ) = when (results) {
        is Loading, Uninitialized -> createLoadingListModels(sectionId, resources)
        is Fail -> createErrorStateListModel(resources.getString(sectionId), results.error)
        is Success -> createSectionSuccessModels(
            sectionId,
            results(),
            baseImageUrl,
            selectedPart,
            clicks,
            resources
        )
    }

    private fun createSectionSuccessModels(
        sectionId: Int,
        results: List<Any>,
        baseImageUrl: String,
        selectedPart: Part,
        clicks: Clicks,
        resources: Resources
    ): List<ListModel> {
        return if (results.isEmpty()) {
            emptyList()
        } else {
            val filteredResults = filterResults(results, selectedPart)

            if (filteredResults.isEmpty()) {
                emptyList()
            } else {
                createSectionHeaderListModel(
                    sectionId,
                    resources
                ) + mapSearchResults(
                    filteredResults,
                    baseImageUrl,
                    selectedPart,
                    clicks,
                    resources
                )
            }
        }
    }

    private fun createSectionHeaderListModel(
        sectionId: Int,
        resources: Resources
    ) =
        listOf(SectionHeaderListModel(resources.getString(sectionId)))

    private fun mapSearchResults(
        results: List<Any>,
        baseImageUrl: String,
        selectedPart: Part,
        clicks: Clicks,
        resources: Resources
    ): List<ListModel> {
        return results
            .map { result ->
                when (result) {
                    is Song -> {
                        SearchResultListModel(
                            result.id,
                            result.name,
                            result.gameName,
                            result.thumbUrl(baseImageUrl, selectedPart),
                            R.drawable.placeholder_sheet
                        ) {
                            clicks.songSearchResult(result.id)
                        }
                    }

                    is Game -> SearchResultListModel(
                        result.id,
                        result.name,
                        generateSubtitleText(result.songs, resources),
                        result.photoUrl,
                        R.drawable.placeholder_game
                    ) { clicks.gameSearchResult(result.id) }

                    is Composer -> SearchResultListModel(
                        result.id,
                        result.name,
                        generateSubtitleText(result.songs, resources),
                        result.photoUrl,
                        R.drawable.placeholder_composer
                    ) { clicks.composerSearchResult(result.id) }

                    else -> throw IllegalArgumentException(
                        "Bad model in search result list."
                    )
                }
            }
    }

    private fun filterResults(results: List<Any>, selectedPart: Part) = results
        .performMappingStep(selectedPart)
        .performFilteringStep(selectedPart)

    private fun List<Any>.performMappingStep(selectedPart: Part) = map {
        when (it) {
            is Song -> it
            is Game -> it.performMappingStep(selectedPart)
            is Composer -> it.performMappingStep(selectedPart)
            else -> throw IllegalArgumentException("ListModel filtering not supported!")
        }
    }

    private fun Game.performMappingStep(selectedPart: Part): Game {
        val availableSongs = songs?.filteredForVocals(selectedPart.apiId)

        return copy(songs = availableSongs)
    }

    private fun Composer.performMappingStep(selectedPart: Part): Composer {
        val availableSongs = songs?.filteredForVocals(selectedPart.apiId)

        return copy(songs = availableSongs)
    }

    private fun List<Any>.performFilteringStep(selectedPart: Part) = filter {
        when (it) {
            is Song -> it.performFilteringStep(selectedPart)
            is Game -> it.performFilteringStep()
            is Composer -> it.performFilteringStep()
            else -> throw IllegalArgumentException(
                "ListModel filtering not supported!"
            )
        }
    }

    private fun Song.performFilteringStep(selectedPart: Part) =
        hasVocals || selectedPart != Part.VOCAL

    private fun Game.performFilteringStep() = songs
        ?.isNotEmpty() ?: false

    private fun Composer.performFilteringStep() = songs
        ?.isNotEmpty() ?: false

    private fun createLoadingListModels(
        sectionId: Int,
        resources: Resources
    ) = createSectionHeaderListModel(sectionId, resources) +
        listOf(
            LoadingImageNameCaptionListModel(resources.getString(sectionId), sectionId)
        )

    private fun createErrorStateListModel(failedOperationName: String, error: Throwable) = listOf(
        SearchErrorStateListModel(
            failedOperationName,
            error.message ?: "Unknown Error",
        )
    )

    @Suppress("LoopWithTooManyJumpStatements")
    private fun generateSubtitleText(
        items: List<Song>?,
        resources: Resources
    ): String {
        if (items.isNullOrEmpty()) return "Error: no values found."

        val builder = StringBuilder()
        var numberOfOthers = items.size

        while (builder.length < MAX_LENGTH_SUBTITLE_CHARS) {
            val index = items.size - numberOfOthers

            if (index >= MAX_LENGTH_SUBTITLE_ITEMS) {
                break
            }

            if (numberOfOthers == 0) {
                break
            }

            if (index != 0) {
                builder.append(resources.getString(R.string.search_subtitle_separator))
            }

            val stringToAppend = items[index].name
            builder.append(stringToAppend)
            numberOfOthers--
        }

        if (numberOfOthers != 0) {
            builder.append(
                resources.getString(
                    R.string.search_subtitle_suffix_others,
                    numberOfOthers
                )
            )
        }

        return builder.toString()
    }

    const val MAX_LENGTH_SUBTITLE_CHARS = 20
    const val MAX_LENGTH_SUBTITLE_ITEMS = 6
}
