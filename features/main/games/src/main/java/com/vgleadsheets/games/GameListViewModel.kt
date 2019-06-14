package com.vgleadsheets.games

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.repository.Storage

class GameListViewModel @AssistedInject constructor(
    @Assisted initialState: GameListState,
    private val repository: Repository
) : MvRxViewModel<GameListState>(initialState) {
    init {
        fetchGames()
    }

    fun onItemClick(position: Int) = withState { state ->
        val data = state.data() as Storage<List<Game>>
        setState { copy(clickedGameId = data.data[position].id) }
    }

    fun onSongListLaunch() {
        setState { copy(clickedGameId = null) }
    }

    private fun fetchGames() {
        repository.getGames()
            .execute { data ->
                copy(data = data)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: GameListState): GameListViewModel
    }

    companion object : MvRxViewModelFactory<GameListViewModel, GameListState> {
        override fun create(viewModelContext: ViewModelContext, state: GameListState): GameListViewModel? {
            val fragment: GameListFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.gameListViewModelFactory.create(state)
        }
    }
}