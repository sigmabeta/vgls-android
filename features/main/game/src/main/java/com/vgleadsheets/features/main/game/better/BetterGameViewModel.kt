package com.vgleadsheets.features.main.game.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class BetterGameViewModel @AssistedInject constructor(
    @Assisted initialState: BetterGameState,
    private val repository: Repository,
) : MvRxViewModel<BetterGameState>(initialState) {
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

    fun onSongClicked(
        id: Long
    ) {
        router.showSongViewer(
            id
        )
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterGameState,
        ): BetterGameViewModel
    }

    companion object : MvRxViewModelFactory<BetterGameViewModel, BetterGameState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterGameState
        ): BetterGameViewModel {
            val fragment: BetterGameFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
