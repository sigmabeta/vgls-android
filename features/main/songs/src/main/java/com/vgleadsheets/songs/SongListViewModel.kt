package com.vgleadsheets.songs

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.repository.Storage

class SongListViewModel @AssistedInject constructor(
    @Assisted initialState: SongListState,
    private val repository: Repository
) : MvRxViewModel<SongListState>(initialState) {
    init {
        fetchSongs()
    }

    fun onItemClick(position: Int) = withState { state ->
        val data = state.data() as Storage<List<Song>>
        setState { copy(clickedSongId = data.data[position].id) }
    }

    fun onSongViewerLaunch() {
        setState { copy(clickedSongId = null) }
    }

    private fun fetchSongs() = withState { state ->
        repository.getSongs(state.gameId)
            .execute { data ->
                copy(data = data)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SongListState): SongListViewModel
    }

    companion object : MvRxViewModelFactory<SongListViewModel, SongListState> {
        override fun create(viewModelContext: ViewModelContext, state: SongListState): SongListViewModel? {
            val fragment: SongListFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.sheetListViewModelFactory.create(state)
        }
    }
}