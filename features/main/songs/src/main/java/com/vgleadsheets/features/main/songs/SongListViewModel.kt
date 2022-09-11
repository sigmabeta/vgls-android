package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.repository.Repository

class SongListViewModel @AssistedInject constructor(
    @Assisted initialState: SongListState,
    private val repository: Repository,
) : MavericksViewModel<SongListState>(initialState) {
    init {
        fetchSongs()
    }

    private fun fetchSongs() {
        repository.getAllSongs(false)
            .execute {
                copy(contentLoad = SongListContent(it))
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: SongListState,
        ): SongListViewModel
    }

    companion object : MavericksViewModelFactory<SongListViewModel, SongListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: SongListState
        ): SongListViewModel {
            val fragment: SongListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
