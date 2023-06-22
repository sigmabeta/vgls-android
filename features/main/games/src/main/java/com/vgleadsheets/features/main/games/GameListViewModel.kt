package com.vgleadsheets.features.main.games

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.features.main.list.mapList
import com.vgleadsheets.repository.VglsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.flowOn

class GameListViewModel @AssistedInject constructor(
    @Assisted initialState: GameListState,
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers
) : MavericksViewModel<GameListState>(initialState) {
    init {
        fetchGames()
    }

    private fun fetchGames() {
        repository.getAllGames()
            .mapList {
                it.copy(
                    songs = repository.getSongsForGameSync(it.id)
                )
            }
            .flowOn(dispatchers.disk)
            .execute {
                copy(contentLoad = GameListContent(it))
            }
    }

    @AssistedFactory
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
