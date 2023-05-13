package com.vgleadsheets.features.main.hud.menu

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.MenuEmptyStateListModel
import com.vgleadsheets.components.MenuErrorStateListModel
import com.vgleadsheets.components.MenuSearchListModel
import com.vgleadsheets.components.MenuSearchMoreListModel
import com.vgleadsheets.components.MenuSectionHeaderListModel
import com.vgleadsheets.components.SearchResultListModel
import com.vgleadsheets.features.main.hud.Clicks
import com.vgleadsheets.features.main.hud.HudMode
import com.vgleadsheets.features.main.hud.R
import com.vgleadsheets.features.main.hud.search.SearchContent
import com.vgleadsheets.images.Page
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals
import java.util.Locale

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
    ): List<ListModel> = if (hudMode == HudMode.SEARCH) {
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

    @Suppress("LongMethod", "ReturnCount")
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
                MenuEmptyStateListModel(
                    R.drawable.ic_search_black_24dp,
                    resources.getString(R.string.search_empty_no_query),
                    showCrossOut = false
                )
            )
        }

        if (query.lowercase().startsWith("stickerbr")) {
            return listOf(
                MenuErrorStateListModel(
                    "stickerbrush",
                    resources.getString(R.string.search_error_search_stickerbrush),
                )
            )
        }

        val gameModels = createSectionModels(
            R.string.search_section_header_games,
            query,
            2,
            contentLoad.games,
            baseImageUrl,
            selectedPart,
            clicks,
            resources
        )
        val songModels = createSectionModels(
            R.string.search_section_header_songs,
            query,
            1,
            contentLoad.songs,
            baseImageUrl,
            selectedPart,
            clicks,
            resources
        )
        val composerModels = createSectionModels(
            R.string.search_section_header_composers,
            query,
            1,
            contentLoad.composers,
            baseImageUrl,
            selectedPart,
            clicks,
            resources
        )

        val loading = createLoadingListModels(
            contentLoad.searching,
            resources
        )

        val listModels = gameModels + songModels + composerModels + loading
        return listModels.ifEmpty {
            listOf(
                MenuEmptyStateListModel(
                    R.drawable.ic_description_24dp,
                    resources.getString(R.string.search_empty_search_no_results),
                )
            )
        }
    }

    private fun createSectionModels(
        sectionId: Int,
        query: String,
        maxResults: Int,
        results: Async<List<Any>>,
        baseImageUrl: String,
        selectedPart: Part,
        clicks: Clicks,
        resources: Resources
    ) = when (results) {
        is Loading, Uninitialized -> emptyList()
        is Fail -> createErrorStateListModel(resources.getString(sectionId), results.error)
        is Success -> createSectionSuccessModels(
            sectionId,
            query,
            maxResults,
            results(),
            baseImageUrl,
            selectedPart,
            clicks,
            resources,
        )
    }

    private fun createSectionSuccessModels(
        sectionId: Int,
        query: String,
        maxResults: Int,
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
                ) + if (filteredResults.size > maxResults) {
                    truncateSearchResults(
                        sectionId,
                        query,
                        maxResults,
                        filteredResults,
                        baseImageUrl,
                        selectedPart,
                        clicks,
                        resources
                    )
                } else {
                    mapSearchResults(
                        filteredResults,
                        baseImageUrl,
                        selectedPart,
                        clicks,
                        resources
                    )
                }
            }
        }
    }

    private fun createSectionHeaderListModel(
        sectionId: Int,
        resources: Resources
    ) = listOf(
        MenuSectionHeaderListModel(
            resources.getString(sectionId)
        )
    )

    private fun truncateSearchResults(
        sectionId: Int,
        query: String,
        maxResults: Int,
        filteredResults: List<Any>,
        baseImageUrl: String,
        selectedPart: Part,
        clicks: Clicks,
        resources: Resources
    ): List<ListModel> {
        return mapSearchResults(
            filteredResults.take(maxResults),
            baseImageUrl,
            selectedPart,
            clicks,
            resources
        ) + MenuSearchMoreListModel(
            resources.getString(
                R.string.search_cta_more,
                filteredResults.size,
                resources.getString(sectionId).lowercase(Locale.getDefault())
            ),
        ) {
            clicks.showMoreResults(query)
        }
    }

    @Suppress("UNUSED_PARAMETER")
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
                            Page.generateThumbUrl(
                                baseImageUrl,
                                selectedPart.apiId,
                                result.isAltSelected,
                                result.filename
                            ),
                            R.drawable.ic_description_24dp
                        ) {
                            clicks.songSearchResult(result.id)
                        }
                    }

                    is Game -> SearchResultListModel(
                        result.id + ID_OFFSET_GAME,
                        result.name,
                        generateSubtitleText(result.songs, resources),
                        result.photoUrl,
                        R.drawable.ic_album_24dp
                    ) { clicks.gameSearchResult(result.id) }

                    is Composer -> SearchResultListModel(
                        result.id + ID_OFFSET_COMPOSER,
                        result.name,
                        generateSubtitleText(result.songs, resources),
                        result.photoUrl,
                        R.drawable.ic_person_24dp
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
        searching: Boolean,
        resources: Resources
    ) = if (!searching) {
        emptyList()
    } else {
        listOf(
            MenuSearchMoreListModel(
                resources.getString(R.string.search_now_loading)
            ) {}
        )
    }

    private fun createErrorStateListModel(failedOperationName: String, error: Throwable) = listOf(
        MenuErrorStateListModel(
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

    private const val MAX_LENGTH_SUBTITLE_CHARS = 20
    private const val MAX_LENGTH_SUBTITLE_ITEMS = 6

    private const val ID_OFFSET_GAME = 1_000L
    private const val ID_OFFSET_COMPOSER = 1_000_000L
}
