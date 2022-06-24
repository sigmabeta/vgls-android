package com.vgleadsheets.features.main.games.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class BetterGameListViewModel @AssistedInject constructor(
    @Assisted initialState: BetterGameListState,
    private val repository: Repository,
) : MvRxViewModel<BetterGameListState>(initialState) {
    init {
        fetchGames()
    }

    private fun fetchGames() {
        repository.getGames()
            .execute {
                copy(contentLoad = BetterGameListContent(it))
            }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onGameClicked(dataId: Long, name: String) {
        router.showSongListForGame(dataId, name)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterGameListState,
        ): BetterGameListViewModel
    }

    companion object : MvRxViewModelFactory<BetterGameListViewModel, BetterGameListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterGameListState
        ): BetterGameListViewModel {
            val fragment: BetterGameListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
