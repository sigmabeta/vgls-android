package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class GameViewModel @AssistedInject constructor(
    @Assisted initialState: GameState,
    private val repository: VglsRepository,
) : MavericksViewModel<GameState>(initialState) {
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
            .execute { songs ->
                copy(
                    contentLoad = contentLoad.copy(
                        songs = songs
                    )
                )
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: GameState,
        ): GameViewModel
    }

    companion object : MavericksViewModelFactory<GameViewModel, GameState> {
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
