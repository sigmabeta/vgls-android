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
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

class SheetDetailViewModel @AssistedInject constructor(
    @Assisted initialState: SheetDetailState,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) : AsyncListViewModel<SheetDetailData, SheetDetailState>(initialState, resourceProvider) {
    init {
        fetchSheetDetail()
    }

    fun clearClicked() {
        ctaHandler.clearClicked()
        detailHandler.clearClicked()
        tagValueHandler.clearClicked()
    }

    override fun defaultLoadingListModel(index: Int): ListModel =
        LoadingNameCaptionListModel("allData", index)

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_list_black_24dp,
        "Sheet not found. Check your internet connection?."
    )

    override fun createSuccessListModels(
        data: SheetDetailData,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ) = createTitleListModel(data.song, selectedPart) +
            createCtaListModels(data.song) +
            createDetailListModels(data.song, selectedPart) +
            createTagValueListModels(data.tagValues)

    private fun createDetailListModels(
        song: Async<Song>,
        selectedPart: PartSelectorItem
    ) = when (song) {
        is Loading, Uninitialized -> createLoadingListModels("details")
        is Fail -> createErrorStateListModel("details", song.error)
        is Success -> createSuccessDetailListModels(song(), selectedPart)
    }

    private fun createSuccessDetailListModels(
        song: Song,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val value = song
            .composers
            ?.map { it.name }
            ?.joinToString(", ") ?: "Unknown Composer"

        val pageCount = song
            .parts
            ?.first { part -> part.name == selectedPart.apiId }
            ?.pages
            ?.size

        val dataId =
            if (song.composers?.size == 1) song.composers?.first()!!.id else ID_COMPOSER_MULTIPLE

        return listOf(
            LabelValueListModel(
                resourceProvider.getString(R.string.label_detail_composer),
                value,
                detailHandler,
                dataId

            ),
            LabelValueListModel(
                resourceProvider.getString(R.string.label_detail_pages),
                pageCount.toString(),
                detailHandler,
                R.string.label_detail_pages.toLong()
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

        val tagValueModels = dedupedTagValues.map {
            LabelValueListModel(
                it.tagKeyName,
                it.name,
                tagValueHandler,
                it.id
            )
        }

        val sectionHeader = listOf(
            SectionHeaderListModel(
                resourceProvider.getString(R.string.section_header_tags)
            )
        )

        return sectionHeader + tagValueModels
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
                R.drawable.ic_favorite_disabled_24,
                resourceProvider.getString(R.string.cta_favorites_add),
                ctaHandler
            ),
            CtaListModel(
                R.drawable.ic_download_24,
                resourceProvider.getString(R.string.cta_make_offline),
                ctaHandler
            )
        )
    }

    private fun createErrorStateListModel(failedOperationName: String, error: Throwable) =
        listOf(ErrorStateListModel(failedOperationName, error.message ?: "Unknown Error"))

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
                        this
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
                        this
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
            val thumbUrl = song()
                .parts
                ?.first { part -> part.name == selectedPart.apiId }
                ?.pages
                ?.first()
                ?.imageUrl

            listOf(
                TitleListModel(
                    song().name,
                    resourceProvider.getString(R.string.subtitle_from_game, song().gameName),
                    thumbUrl,
                    R.drawable.placeholder_sheet
                )
            )
        }
    }

    private val detailHandler = object : LabelValueListModel.EventHandler {
        override fun onClicked(clicked: LabelValueListModel) = setState {
            copy(clickedDetailModel = clicked)
        }

        override fun clearClicked() = setState { copy(clickedDetailModel = null) }
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
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SheetDetailState): SheetDetailViewModel
    }

    companion object : MvRxViewModelFactory<SheetDetailViewModel, SheetDetailState> {
        const val ID_COMPOSER_MULTIPLE = Long.MAX_VALUE

        override fun create(
            viewModelContext: ViewModelContext,
            state: SheetDetailState
        ): SheetDetailViewModel? {
            val fragment: SheetDetailFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.sheetViewModelFactory.create(state)
        }
    }
}
