package com.vgleadsheets.features.main.game

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class GameDetailViewModel @AssistedInject constructor(
    @Assisted initialState: GameDetailState,
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
) : MavericksViewModel<GameDetailState>(initialState) {
    init {
        fetchGame()
        fetchSongsAndComposers()
    }

    fun onFavoriteClick() = withState { state ->
        viewModelScope.launch(dispatchers.disk) {
            repository.toggleFavoriteGame(
                state.gameId
            )
        }
    }

    private fun fetchGame() = withState {
        repository.getGame(it.gameId)
            .execute {
                copy(
                    game = it
                )
            }
    }

    private fun fetchSongsAndComposers() = withState { state ->
        repository.getSongsForGame(state.gameId)
            .execute { songs ->
                val composers = when (songs) {
                    is Uninitialized -> Uninitialized
                    is Fail -> Fail(songs.error)
                    is Loading -> Loading()
                    is Success -> {
                        Success(
                            songs()
                                .flatMap { it.composers.orEmpty() }
                                .distinctBy { it.id }
                        )
                    }
                }

                copy(
                    songs = songs,
                    composers = composers
                )
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: GameDetailState,
        ): GameDetailViewModel
    }

    companion object : MavericksViewModelFactory<GameDetailViewModel, GameDetailState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: GameDetailState
        ): GameDetailViewModel {
            val fragment: GameDetailFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
