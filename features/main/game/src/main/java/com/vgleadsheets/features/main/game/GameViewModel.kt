package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class GameViewModel @AssistedInject constructor(
    @Assisted initialState: GameState,
    private val repository: Repository,
) : MvRxViewModel<GameState>(initialState) {
    init {
        fetchGame()
        fetchSongs()
    }

    private fun fetchGame() = withState {
        repository.getGame(it.gameId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        game = it
                    )
                )
            }
    }

    private fun fetchSongs() = withState {
        repository.getSongsForGame(it.gameId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        songs = it
                    )
                )
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: GameState,
        ): GameViewModel
    }

    companion object : MvRxViewModelFactory<GameViewModel, GameState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: GameState
        ): GameViewModel {
            val fragment: GameFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
