package com.vgleadsheets.features.main.favorites

import android.content.res.Resources
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingImageNameCaptionListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.features.main.hud.HudState
import com.vgleadsheets.features.main.list.BetterListConfig
import com.vgleadsheets.features.main.list.BetterListConfig.Companion.MAX_LENGTH_SUBTITLE_CHARS
import com.vgleadsheets.features.main.list.BetterListConfig.Companion.MAX_LENGTH_SUBTITLE_ITEMS
import com.vgleadsheets.features.main.list.LoadingItemStyle
import com.vgleadsheets.features.main.list.isNullOrEmpty
import com.vgleadsheets.features.main.list.mapYielding
import com.vgleadsheets.features.main.list.sections.Actions
import com.vgleadsheets.features.main.list.sections.Content
import com.vgleadsheets.features.main.list.sections.EmptyState
import com.vgleadsheets.features.main.list.sections.ErrorState
import com.vgleadsheets.features.main.list.sections.LoadingState
import com.vgleadsheets.features.main.list.sections.Title
import com.vgleadsheets.images.Page
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker

class Config(
    private val state: FavoriteListState,
    private val hudState: HudState,
    private val baseImageUrl: String,
    private val clicks: Clicks,
    private val perfTracker: PerfTracker,
    private val perfSpec: PerfSpec,
    private val resources: Resources
) : BetterListConfig {
    override val titleConfig = Title.Config(
        resources.getString(R.string.app_name),
        resources.getString(R.string.label_favorites),
        resources,
        {
            perfTracker.onTitleLoaded(perfSpec)
            perfTracker.onTransitionStarted(perfSpec)
        },
        { },
        onMenuButtonClick = { clicks.menu() }
    )

    override val actionsConfig = Actions.NONE

    override val contentConfig = Content.Config(
        !state.contentLoad.isNullOrEmpty()
    ) {
        val songModels = createSectionModels(
            R.string.section_header_songs,
            state.contentLoad.songsLoad,
            hudState.selectedPart
        )
        val gameModels = createSectionModels(
            R.string.section_header_games,
            state.contentLoad.gamesLoad,
            hudState.selectedPart
        )
        val composerModels = createSectionModels(
            R.string.section_header_composers,
            state.contentLoad.composerLoad,
            hudState.selectedPart
        )

        val listModels = songModels + gameModels + composerModels
        return@Config listModels.ifEmpty {
            listOf(
                EmptyStateListModel(
                    R.drawable.ic_jam_filled,
                    resources.getString(R.string.empty_no_favorites),
                )
            )
        }
    }

    override val emptyConfig = EmptyState.Config(
        state.isEmpty(),
        R.drawable.ic_album_24dp,
        resources.getString(R.string.missing_thing_favorite)
    )

    override val errorConfig = ErrorState.Config(
        state.hasFailed(),
        BuildConfig.DEBUG, // TODO inject this
        FavoriteListFragment.LOAD_OPERATION,
        state.failure()?.message ?: resources.getString(R.string.error_dev_unknown)
    )

    override val loadingConfig = LoadingState.Config(
        state.isLoading(),
        LoadingItemStyle.WITH_IMAGE
    )

    private suspend fun createSectionModels(
        sectionId: Int,
        results: Async<List<Any>>,
        selectedPart: Part
    ) = when (results) {
        is Loading, Uninitialized -> createLoadingListModels(sectionId)
        is Fail -> createErrorStateListModel(resources.getString(sectionId), results.error)
        is Success -> createSectionSuccessModels(
            sectionId,
            results(),
            selectedPart
        )
    }

    private suspend fun createSectionSuccessModels(
        sectionId: Int,
        results: List<Any>,
        selectedPart: Part
    ): List<ListModel> {
        return if (results.isEmpty()) {
            emptyList()
        } else {
            val filteredResults = filterResults(results, selectedPart)

            if (filteredResults.isEmpty()) {
                emptyList()
            } else {
                createSectionHeaderListModel(sectionId) + createSectionModels(filteredResults)
            }
        }
    }

    private fun createSectionHeaderListModel(sectionId: Int) =
        listOf(SectionHeaderListModel(resources.getString(sectionId)))

    private suspend fun createSectionModels(
        results: List<Any>
    ): List<ListModel> {
        return results
            .mapYielding { result ->
                when (result) {
                    is Song -> {
                        ImageNameCaptionListModel(
                            result.id,
                            result.name,
                            result.gameName,
                            Page.generateThumbUrl(
                                baseImageUrl,
                                hudState.selectedPart.apiId,
                                result.isAltSelected,
                                result.filename
                            ),
                            R.drawable.ic_description_24dp
                        ) {
                            clicks.song(result.id)
                        }
                    }

                    is Game -> ImageNameCaptionListModel(
                        result.id + ID_OFFSET_GAME,
                        result.name,
                        generateSubtitleText(result.songs),
                        result.photoUrl,
                        R.drawable.ic_album_24dp
                    ) { clicks.game(result.id) }

                    is Composer -> ImageNameCaptionListModel(
                        result.id + ID_OFFSET_COMPOSER,
                        result.name,
                        generateSubtitleText(result.songs),
                        result.photoUrl,
                        R.drawable.ic_person_24dp
                    ) { clicks.composer(result.id) }

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

    private fun createLoadingListModels(sectionId: Int) = createSectionHeaderListModel(sectionId) +
        listOf(
            LoadingImageNameCaptionListModel(resources.getString(sectionId), sectionId)
        )

    private fun createErrorStateListModel(failedOperationName: String, error: Throwable) = listOf(
        ErrorStateListModel(
            failedOperationName,
            error.message ?: "Unknown Error",
        )
    )

    @Suppress("LoopWithTooManyJumpStatements")
    private fun generateSubtitleText(items: List<Song>?): String {
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
                builder.append(resources.getString(R.string.subtitle_separator))
            }

            val stringToAppend = items[index].name
            builder.append(stringToAppend)
            numberOfOthers--
        }

        if (numberOfOthers != 0) {
            builder.append(
                resources.getString(
                    R.string.subtitle_suffix_others,
                    numberOfOthers
                )
            )
        }

        return builder.toString()
    }

    companion object {
        private const val ID_OFFSET_GAME = 1_000L
        private const val ID_OFFSET_COMPOSER = 1_000_000L
    }
}
