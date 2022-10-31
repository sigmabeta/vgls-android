package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MavericksViewModel
import com.vgleadsheets.repository.Repository

class GameViewModel @AssistedInject constructor(
    @Assisted initialState: GameState,
    private val repository: Repository,
) : MavericksViewModel<GameState>(initialState) {
    init {
        fetchGame()
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


        @AssistedInject.Factory
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
