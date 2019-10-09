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
    private val repository: Repository
) : MvRxViewModel<GameState>(initialState) {
    init {
        fetchGame()
        fetchSongs()
    }

    fun onGbGameNotChecked(vglsId: Long, name: String) {
        repository.searchGiantBombForGame(vglsId, name)
    }

    private fun fetchGame() = withState { state ->
        repository
            .getGame(state.gameId)
            .execute { game ->
                copy(game = game)
            }
    }

    private fun fetchSongs() = withState { state ->
        repository
            .getSongsForGame(state.gameId)
            .execute { songs ->
                copy(songs = songs)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: GameState): GameViewModel
    }

    companion object : MvRxViewModelFactory<GameViewModel, GameState> {
        override fun create(viewModelContext: ViewModelContext, state: GameState): GameViewModel? {
            val fragment: GameFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.gameViewModelFactory.create(state)
        }
    }
}
