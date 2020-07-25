package com.vgleadsheets.features.main.composer

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
import com.vgleadsheets.model.composer.Composer
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

@SuppressWarnings("TooManyFunctions")
class ComposerViewModel @AssistedInject constructor(
    @Assisted initialState: ComposerState,
    @Assisted val screenName: String,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider,
    private val perfTracker: PerfTracker
) : AsyncListViewModel<ComposerData, ComposerState>(initialState, perfTracker),
    ImageNameCaptionListModel.EventHandler {
    init {
        fetchComposer()
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
        data: ComposerData,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val title = createTitleListModel(data.composer, data.songs)
        val content = createContentListModels(data.songs, selectedPart)

        return title + content
    }

    private fun fetchComposer() = withState { state ->
        repository
            .getComposer(state.composerId)
            .execute { composer ->
                val newData = ComposerData(composer, data.songs)
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
            .getSongsByComposer(state.composerId)
            .execute { songs ->
                val newData = ComposerData(data.composer, songs)
                updateListState(
                    data = newData,
                    listModels = constructList(
                        newData,
                        this
                    )
                )
            }
    }

    private fun createTitleListModel(composer: Async<Composer>, songs: Async<List<Song>>): List<ListModel> =
        when (composer) {
            is Success -> listOf(
                TitleListModel(
                    composer().name,
                    generateSheetCountText(songs),
                    composer().photoUrl,
                    R.drawable.placeholder_composer,
                    screenName = screenName,
                    tracker = perfTracker
                )
            )
            is Fail -> createErrorStateListModel(composer.error)
            is Uninitialized, is Loading -> listOf(
                LoadingTitleListModel()
            )
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
                    this@ComposerViewModel,
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
        fun create(initialState: ComposerState, screenName: String): ComposerViewModel
    }

    companion object : MvRxViewModelFactory<ComposerViewModel, ComposerState> {
        override fun create(viewModelContext: ViewModelContext, state: ComposerState): ComposerViewModel? {
            val fragment: ComposerFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.composerViewModelFactory.create(state, fragment.getPerfScreenName())
        }
    }
}
