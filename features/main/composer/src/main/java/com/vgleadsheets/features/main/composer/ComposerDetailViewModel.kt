package com.vgleadsheets.features.main.composer

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.features.main.list.mapList
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class ComposerDetailViewModel @AssistedInject constructor(
    @Assisted initialState: ComposerDetailState,
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
) : MavericksViewModel<ComposerDetailState>(initialState) {
    init {
        fetchComposer()
        fetchSongsAndGames()
    }

    fun onFavoriteClick() = withState { state ->
        viewModelScope.launch(dispatchers.disk) {
            repository.toggleFavoriteComposer(
                state.composerId
            )
        }
    }

    private fun fetchComposer() = withState { state ->
        repository.getComposer(state.composerId)
            .execute { composer ->
                copy(
                    composer = composer,
                )
            }
    }

    private fun fetchSongsAndGames() = withState { state ->
        repository
            .getSongsForComposer(state.composerId)
            .mapList { it.copy(game = repository.getGameSync(it.gameId)) }
            .flowOn(dispatchers.disk)
            .execute { songs ->
                val games = when (songs) {
                    is Uninitialized -> Uninitialized
                    is Fail -> Fail(songs.error)
                    is Loading -> Loading()
                    is Success -> {
                        Success(
                            songs()
                                .mapNotNull { it.game }
                                .distinctBy { it.id }
                        )
                    }
                }

                copy(
                    songs = songs,
                    games = games
                )
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: ComposerDetailState
        ): ComposerDetailViewModel
    }

    companion object : MavericksViewModelFactory<ComposerDetailViewModel, ComposerDetailState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: ComposerDetailState
        ): ComposerDetailViewModel {
            val fragment: ComposerDetailFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}
