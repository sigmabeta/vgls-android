package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.ListViewModel
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.resources.ResourceProvider

class SongListViewModel @AssistedInject constructor(
    @Assisted initialState: SongListState,
    private val repository: Repository,
    private val resourceProvider: ResourceProvider
) : ListViewModel<Song, SongListState>(initialState, resourceProvider),
    ImageNameCaptionListModel.EventHandler {
    init {
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

    override fun createTitleListModel() = TitleListModel(
        resourceProvider.getString(R.string.app_name),
        resourceProvider.getString(R.string.subtitle_all_sheets),
        perfHandler = perfHandler
    )

    override fun createFullEmptyStateListModel() = EmptyStateListModel(
        R.drawable.ic_album_24dp,
        "No songs found at all. Check your internet connection?"
    )

    override fun createSuccessListModels(
        data: List<Song>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val availableSongs = filterSongs(data, selectedPart)

        return if (availableSongs.isEmpty()) {
            arrayListOf(
                EmptyStateListModel(
                    R.drawable.ic_album_24dp,
                    "No songs found with a ${selectedPart.apiId} part. Try another part?"
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
                    this,
                    perfHandler = perfHandler
                )
            }
        }
    }

    private fun fetchSongs() {
        repository.getAllSongs()
            .execute { data ->
                copy(
                    data = data,
                    listModels = constructList(
                        data,
                        updateTime,
                        digest,
                        selectedPart
                    )
                )
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
        fun create(initialState: SongListState): SongListViewModel
    }

    companion object : MvRxViewModelFactory<SongListViewModel, SongListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SongListState
        ): SongListViewModel? {
            val fragment: SongListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.sheetListViewModelFactory.create(state)
        }
    }
}
