package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SongListViewModel @AssistedInject constructor(
    @Assisted initialState: SongListState,
    private val repository: VglsRepository,
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

    @AssistedFactory
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
