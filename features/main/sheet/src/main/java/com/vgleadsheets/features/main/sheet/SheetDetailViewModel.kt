package com.vgleadsheets.features.main.sheet

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.LabelRatingStarListModel
import com.vgleadsheets.components.LabelValueListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.LoadingTitleListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListViewModel
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

@SuppressWarnings("TooManyFunctions")
class SheetDetailViewModel @AssistedInject constructor(
    @Assisted initialState: SheetDetailState,
    @Assisted val screenName: String,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val perfTracker: PerfTracker
) : AsyncListViewModel<SheetDetailData, SheetDetailState>(initialState, screenName, perfTracker) {
    init {
        fetchSheetDetail()
    }

    fun clearClicked() {
        ctaHandler.clearClicked()
        composerHandler.clearClicked()
        gameHandler.clearClicked()
        ratingStarHandler.clearClicked()
        tagValueHandler.clearClicked()
    }

    override fun defaultLoadingListModel(index: Int): ListModel =
        LoadingNameCaptionListModel("allData", index)

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_list_black_24dp,
        "Sheet not found. Check your internet connection?.",
        screenName,
        cancelPerfOnEmptyState
    )

    override fun createSuccessListModels(
        data: SheetDetailData,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ) = createTitleListModel(data.song, selectedPart) +
        createCtaListModels(data.song) +
        createDetailListModels(data.song) +
        createTagValueListModels(data.tagValues)

    private fun createDetailListModels(
        song: Async<Song>
    ) = when (song) {
        is Loading, Uninitialized -> createLoadingListModels("details")
        is Fail -> createErrorStateListModel("details", song.error)
        is Success -> createSuccessDetailListModels(song())
    }

    private fun createSuccessDetailListModels(
        song: Song
    ): List<ListModel> {
        val value = song
            .composers
            ?.map { it.name }
            ?.joinToString(", ") ?: "Unknown Composer"

        val composerId =
            if (song.composers?.size == 1) song.composers?.first()!!.id else ID_COMPOSER_MULTIPLE

        return listOf(
            LabelValueListModel(
                resourceProvider.getString(R.string.label_detail_composer),
                value,
                screenName,
                composerHandler,
                composerId
            ),
            LabelValueListModel(
                resourceProvider.getString(R.string.label_detail_game),
                song.gameName,
                screenName,
                gameHandler,
                song.gameId
            )
        )
    }

    private fun createTagValueListModels(tagValues: Async<List<TagValue>>) = when (tagValues) {
        is Loading, Uninitialized -> createLoadingListModels("tagValues")
        is Fail -> createErrorStateListModel("tagValues", tagValues.error)
        is Success -> createSuccessTagValueListModels(tagValues())
    }

    private fun createSuccessTagValueListModels(tagValues: List<TagValue>): List<ListModel> {
        val dupedTagValueGroups = tagValues
            .groupBy { it.tagKeyName }
            .filter { it.value.size > 1 }

        val dedupedTagValues = if (dupedTagValueGroups.isEmpty()) {
            tagValues
        } else {
            val tempValues = tagValues.toMutableList()

            dupedTagValueGroups.forEach { entry ->
                val dupesWithThisKey = entry.value
                tempValues.removeAll(dupesWithThisKey)

                val renamedDupesWithThisKey = dupesWithThisKey
                    .mapIndexed { index, originalValue ->
                        TagValue(
                            originalValue.id,
                            originalValue.name,
                            "${originalValue.tagKeyName} ${index + 1}",
                            originalValue.songs
                        )
                    }

                tempValues.addAll(renamedDupesWithThisKey)
            }

            tempValues.sortBy { it.tagKeyName }
            tempValues.toList()
        }

        val unsortedModels = dedupedTagValues.map {
            val valueAsNumber = it.name.toIntOrNull() ?: -1

            return@map if (valueAsNumber in RATING_MINIMUM..RATING_MAXIMUM) {
                LabelRatingStarListModel(
                    it.tagKeyName,
                    valueAsNumber,
                    screenName,
                    ratingStarHandler,
                    it.id
                )
            } else {
                LabelValueListModel(
                    it.tagKeyName,
                    it.name,
                    screenName,
                    tagValueHandler,
                    it.id
                )
            }
        }

        val sortedModels = unsortedModels
            .sortedBy { it.javaClass.simpleName }

        val sectionHeader = listOf(
            SectionHeaderListModel(
                resourceProvider.getString(R.string.section_header_tags)
            )
        )

        return sectionHeader + sortedModels
    }

    private fun createLoadingListModels(sectionId: String) = listOf(
        LoadingNameCaptionListModel(sectionId, sectionId.hashCode())
    )

    private fun createCtaListModels(song: Async<Song>) = when (song) {
        is Loading, Uninitialized -> createLoadingListModels("Cta")
        is Fail, is Success -> listOf(
            CtaListModel(
                R.drawable.ic_description_24dp,
                resourceProvider.getString(R.string.cta_view_sheet),
                ctaHandler
            ),
            CtaListModel(
                R.drawable.ic_play_circle_filled_24,
                resourceProvider.getString(R.string.cta_youtube),
                ctaHandler
            )
        )
    }

    private fun createErrorStateListModel(failedOperationName: String, error: Throwable) =
        listOf(
            ErrorStateListModel(
                failedOperationName,
                error.message ?: "Unknown Error",
                screenName,
                cancelPerfOnErrorState
            )
        )

    private fun fetchSheetDetail() = withState { state ->
        val songId = state.songId

        repository.getSong(songId, true)
            .execute { newSheetDetail ->
                val newData = data.copy(
                    song = newSheetDetail
                )
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        digest,
                        updateTime,
                        selectedPart
                    )
                )
            }

        repository.getTagValuesForSong(songId)
            .execute { tagValues ->
                val newData = data.copy(
                    tagValues = tagValues
                )
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        digest,
                        updateTime,
                        selectedPart
                    )
                )
            }
    }

    private fun createTitleListModel(
        song: Async<Song>,
        selectedPart: PartSelectorItem
    ) = when (song) {
        is Loading, Uninitialized -> listOf(LoadingTitleListModel())
        is Fail -> createErrorStateListModel("title", song.error)
        is Success -> {
            val pages = song()
                .parts
                ?.first { part -> part.name == selectedPart.apiId }
                ?.pages

            val pageCount = pages?.size

            val thumbUrl = pages
                ?.first()
                ?.imageUrl

            listOf(
                TitleListModel(
                    song().name,
                    resourceProvider.getString(R.string.subtitle_pages, pageCount),
                    thumbUrl,
                    R.drawable.placeholder_sheet,
                    screenName = screenName,
                    tracker = perfTracker
                )
            )
        }
    }

    private val composerHandler = object : LabelValueListModel.EventHandler {
        override fun onClicked(clicked: LabelValueListModel) = setState {
            copy(clickedComposerModel = clicked)
        }

        override fun clearClicked() = setState { copy(clickedComposerModel = null) }

        override fun onLabelValueLoaded(screenName: String) {
            perfTracker.onPartialContentLoad(screenName)
        }
    }

    private val gameHandler = object : LabelValueListModel.EventHandler {
        override fun onClicked(clicked: LabelValueListModel) = setState {
            copy(clickedGameModel = clicked)
        }

        override fun clearClicked() = setState { copy(clickedGameModel = null) }

        override fun onLabelValueLoaded(screenName: String) = Unit
    }

    private val ctaHandler = object : CtaListModel.EventHandler {
        override fun onClicked(clicked: CtaListModel) = setState {
            copy(clickedCtaModel = clicked)
        }

        override fun clearClicked() = setState { copy(clickedCtaModel = null) }
    }

    private val tagValueHandler = object : LabelValueListModel.EventHandler {
        override fun onClicked(clicked: LabelValueListModel) = setState {
            copy(clickedTagValueModel = clicked)
        }

        override fun clearClicked() = setState { copy(clickedTagValueModel = null) }

        override fun onLabelValueLoaded(screenName: String) = Unit
    }

    private val ratingStarHandler = object : LabelRatingStarListModel.EventHandler {
        override fun onClicked(clicked: LabelRatingStarListModel) = setState {
            copy(clickedRatingStarModel = clicked)
        }

        override fun clearClicked() = setState { copy(clickedRatingStarModel = null) }

        override fun onRatingStarsLoaded(screenName: String) {
            perfTracker.onFullContentLoad(screenName)
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SheetDetailState, screenName: String): SheetDetailViewModel
    }

    companion object : MvRxViewModelFactory<SheetDetailViewModel, SheetDetailState> {
        const val ID_COMPOSER_MULTIPLE = Long.MAX_VALUE

        const val RATING_MINIMUM = 0
        const val RATING_MAXIMUM = 5

        override fun create(
            viewModelContext: ViewModelContext,
            state: SheetDetailState
        ): SheetDetailViewModel? {
            val fragment: SheetDetailFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.sheetViewModelFactory.create(state, fragment.getPerfScreenName())
        }
    }
}
