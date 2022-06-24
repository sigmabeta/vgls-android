package com.vgleadsheets.features.main.songs.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class BetterSongListViewModel @AssistedInject constructor(
    @Assisted initialState: BetterSongListState,
    private val repository: Repository,
) : MvRxViewModel<BetterSongListState>(initialState) {
    init {
        fetchSongs()
    }

    fun onSongClicked(
        id: Long
    ) {
        router.showSongViewer(
            id
        )
    }

    private fun fetchSongs() {
        repository.getAllSongs(false)
            .execute {
                copy(contentLoad = BetterSongListContent(it))
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterSongListState,
        ): BetterSongListViewModel
    }

    companion object : MvRxViewModelFactory<BetterSongListViewModel, BetterSongListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterSongListState
        ): BetterSongListViewModel {
            val fragment: BetterSongListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
