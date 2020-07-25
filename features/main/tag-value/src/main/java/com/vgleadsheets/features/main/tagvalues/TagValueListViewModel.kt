package com.vgleadsheets.features.main.tagvalues

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
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.LoadingTitleListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.ListViewModel.Companion.MAX_LENGTH_SUBTITLE_CHARS
import com.vgleadsheets.features.main.list.ListViewModel.Companion.MAX_LENGTH_SUBTITLE_ITEMS
import com.vgleadsheets.features.main.list.async.AsyncListViewModel
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagKey
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

@SuppressWarnings("TooManyFunctions")
class TagValueListViewModel @AssistedInject constructor(
    @Assisted initialState: TagValueListState,
    @Assisted val screenName: String,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val perfTracker: PerfTracker
) : AsyncListViewModel<TagValueData, TagValueListState>(initialState, perfTracker),
    NameCaptionListModel.EventHandler {
    init {
        fetchTagKey()
        fetchTagValues()
    }

    override fun onClicked(clicked: NameCaptionListModel) = setState {
        copy(
            clickedListModel = clicked
        )
    }

    override fun clearClicked() = setState {
        copy(
            clickedListModel = null
        )
    }

    override fun defaultLoadingListModel(index: Int): ListModel =
        LoadingNameCaptionListModel("allData", index)

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_album_24dp,
        "No tag values found at all. Check your internet connection?",
        screenName,
        cancelPerfOnEmptyState
    )

    override fun createSuccessListModels(
        data: TagValueData,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val title = createTitleListModel(data.tagKey, data.tagValues)
        val content = createContentListModels(data.tagValues, selectedPart)

        return title + content
    }

    private fun fetchTagKey() = withState { state ->
        repository
            .getTagKey(state.tagKeyId)
            .execute { tagKey ->
                val newData = TagValueData(tagKey, data.tagValues)
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        this
                    )
                )
            }
    }

    private fun fetchTagValues() = withState { state ->
        repository
            .getTagValuesForTagKey(state.tagKeyId)
            .execute { tagValues ->
                val newData = TagValueData(data.tagKey, tagValues)
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
        tagKey: Async<TagKey>,
        tagValues: Async<List<TagValue>>
    ): List<ListModel> =
        when (tagKey) {
            is Success -> listOf(
                TitleListModel(
                    tagKey().name,
                    generateSheetCountText(tagValues),
                    screenName = screenName,
                    tracker = perfTracker
                )
            )
            is Fail -> createErrorStateListModel(tagKey.error)
            is Uninitialized, is Loading -> listOf(LoadingTitleListModel())
            else -> createErrorStateListModel(IllegalStateException("Unhandled title state."))
        }

    private fun generateSheetCountText(
        tagValues: Async<List<TagValue>>
    ) = if (tagValues is Success) {
        resourceProvider.getString(R.string.subtitle_options_count, tagValues().size)
    } else ""

    private fun createContentListModels(
        tagValues: Async<List<TagValue>>,
        selectedPart: PartSelectorItem
    ) = when (tagValues) {
        is Success -> createTagValueListModels(tagValues(), selectedPart)
        is Fail -> createErrorStateListModel(tagValues.error)
        is Uninitialized, is Loading -> createLoadingListModels()
    }

    private fun createTagValueListModels(
        tagValues: List<TagValue>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val availableTagValues = filterTagValues(tagValues, selectedPart)

        return if (availableTagValues.isEmpty()) {
            arrayListOf(
                EmptyStateListModel(
                    R.drawable.ic_album_24dp,
                    "No tag values found with a ${selectedPart.apiId} part. Try another part?",
                    screenName,
                    cancelPerfOnEmptyState
                )
            )
        } else {
            availableTagValues.map {
                NameCaptionListModel(
                    it.id,
                    it.name,
                    generateSheetCaption(it.songs),
                    this@TagValueListViewModel,
                    screenName = screenName,
                    tracker = perfTracker
                )
            }
        }
    }

    private fun filterTagValues(
        tagValues: List<TagValue>,
        selectedPart: PartSelectorItem
    ) = tagValues.map { tagValue ->
        val availableSongs = tagValue.songs?.filter { song ->
            song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
        }

        tagValue.copy(songs = availableSongs)
    }.filter {
        it.songs?.isNotEmpty() ?: false
    }

    private fun generateSheetCaption(songs: List<Song>?): String {
        if (songs.isNullOrEmpty()) return "Error: no values found."

        val builder = StringBuilder()
        var numberOfOthers = songs.size

        while (builder.length < MAX_LENGTH_SUBTITLE_CHARS) {
            val index = songs.size - numberOfOthers

            if (index >= MAX_LENGTH_SUBTITLE_ITEMS) {
                break
            }

            if (numberOfOthers == 0) {
                break
            }

            if (index != 0) {
                builder.append(resourceProvider.getString(R.string.subtitle_separator))
            }

            val stringToAppend = songs[index].name
            builder.append(stringToAppend)
            numberOfOthers--
        }

        if (numberOfOthers != 0) {
            builder.append(
                resourceProvider.getString(
                    R.string.subtitle_suffix_others,
                    numberOfOthers
                )
            )
        }

        return builder.toString()
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: TagValueListState, screenName: String): TagValueListViewModel
    }

    companion object : MvRxViewModelFactory<TagValueListViewModel, TagValueListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TagValueListState
        ): TagValueListViewModel? {
            val fragment: TagValueListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.tagValueListViewModelFactory.create(state, fragment.getPerfScreenName())
        }
    }
}
