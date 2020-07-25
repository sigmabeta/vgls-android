package com.vgleadsheets.features.main.tagsongs

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
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingTitleListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListViewModel
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

@SuppressWarnings("TooManyFunctions")
class TagValueSongListViewModel @AssistedInject constructor(
    @Assisted initialState: TagValueSongListState,
    @Assisted val screenName: String,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val perfTracker: PerfTracker
) : AsyncListViewModel<TagValueSongListData, TagValueSongListState>(initialState, screenName, perfTracker),
    ImageNameCaptionListModel.EventHandler {
    init {
        fetchTagValue()
        fetchSongs()
    }

    override fun onClicked(clicked: ImageNameCaptionListModel) = setState {
        copy(
            clickedListModel = clicked
        )
    }

    override fun clearClicked() = setState {
        copy(
            clickedListModel = null
        )
    }

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_album_24dp,
        "No songs found at all. Check your internet connection?",
        screenName,
        cancelPerfOnEmptyState
    )

    override fun createSuccessListModels(
        data: TagValueSongListData,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val title = createTitleListModel(data.tagValue, data.songs)
        val content = createContentListModels(data.songs, selectedPart)

        return title + content
    }

    private fun fetchTagValue() = withState { state ->
        repository
            .getTagValue(state.tagValueId)
            .execute { tagValue ->
                val newData = TagValueSongListData(tagValue, data.songs)
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        this
                    )
                )
            }
    }

    private fun fetchSongs() = withState { state ->
        repository
            .getSongsForTagValue(state.tagValueId)
            .execute { songs ->
                val newData = TagValueSongListData(data.tagValue, songs)
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
        tagValue: Async<TagValue>,
        songs: Async<List<Song>>
    ): List<ListModel> =
        when (tagValue) {
            is Success -> listOf(
                TitleListModel(
                    resourceProvider.getString(R.string.title_tag_value_songs, tagValue().tagKeyName, tagValue().name),
                    generateSheetCountText(songs),
                    screenName = screenName,
                    tracker = perfTracker
                )
            )
            is Fail -> createErrorStateListModel(tagValue.error)
            is Uninitialized, is Loading -> listOf(LoadingTitleListModel())
            else -> createErrorStateListModel(IllegalStateException("Unhandled title state."))
        }

    private fun generateSheetCountText(
        songs: Async<List<Song>>
    ) = if (songs is Success) {
        resourceProvider.getString(R.string.subtitle_sheets_count, songs().size)
    } else ""

    private fun createContentListModels(
        songs: Async<List<Song>>,
        selectedPart: PartSelectorItem
    ) = when (songs) {
        is Success -> createSongListModels(songs(), selectedPart)
        is Fail -> createErrorStateListModel(songs.error)
        is Uninitialized, is Loading -> createLoadingListModels()
    }

    private fun createSongListModels(
        songs: List<Song>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val availableSongs = filterSongs(songs, selectedPart)

        return if (availableSongs.isEmpty()) {
            arrayListOf(
                EmptyStateListModel(
                    R.drawable.ic_album_24dp,
                    "No songs found with a ${selectedPart.apiId} part. Try another part?",
                    screenName,
                    cancelPerfOnEmptyState
                )
            )
        } else {
            availableSongs.map {
                val thumbUrl = it
                    .parts
                    ?.first { part -> part.name == selectedPart.apiId }
                    ?.pages
                    ?.first()
                    ?.imageUrl

                ImageNameCaptionListModel(
                    it.id,
                    it.name,
                    it.gameName,
                    thumbUrl,
                    R.drawable.placeholder_sheet,
                    this@TagValueSongListViewModel,
                    screenName = screenName,
                    tracker = perfTracker
                )
            }
        }
    }

    private fun filterSongs(
        songs: List<Song>,
        selectedPart: PartSelectorItem
    ) = songs.filter { song ->
        song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: TagValueSongListState, screenName: String): TagValueSongListViewModel
    }

    companion object : MvRxViewModelFactory<TagValueSongListViewModel, TagValueSongListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: TagValueSongListState
        ): TagValueSongListViewModel? {
            val fragment: TagValueSongListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.tagValueViewModelFactory.create(state, fragment.getPerfScreenName())
        }
    }
}
