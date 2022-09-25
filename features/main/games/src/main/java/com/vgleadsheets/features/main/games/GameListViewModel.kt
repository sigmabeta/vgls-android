package com.vgleadsheets.features.main.games

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.repository.Repository

class GameListViewModel @AssistedInject constructor(
    @Assisted initialState: GameListState,
    private val repository: Repository,
) : MavericksViewModel<GameListState>(initialState) {
    init {
        fetchGames()
    }

    private fun fetchGames() {
        repository.getGames()
            .execute {
                copy(contentLoad = GameListContent(it))
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: GameListState,
        ): GameListViewModel
    }

    companion object : MavericksViewModelFactory<GameListViewModel, GameListState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: GameListState
        ): GameListViewModel {
            val fragment: GameListFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
