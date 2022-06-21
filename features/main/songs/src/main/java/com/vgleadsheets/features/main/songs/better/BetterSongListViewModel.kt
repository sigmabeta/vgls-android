package com.vgleadsheets.features.main.songs.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.tracking.TrackingScreen

class BetterSongListViewModel @AssistedInject constructor(
    @Assisted initialState: BetterSongListState,
    @Assisted private val router: FragmentRouter,
    private val repository: Repository,
) : MvRxViewModel<BetterSongListState>(initialState) {
    init {
        fetchSongs()
    }

    fun onSongClicked(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String
    ) {
        router.showSongViewer(
            id,
            songName,
            gameName,
            transposition,
            TrackingScreen.LIST_SHEET,
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
            router: FragmentRouter
        ): BetterSongListViewModel
    }

    companion object : MvRxViewModelFactory<BetterSongListViewModel, BetterSongListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterSongListState
        ): BetterSongListViewModel {
            val fragment: BetterSongListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state, fragment.activity as FragmentRouter)
        }
    }
}
