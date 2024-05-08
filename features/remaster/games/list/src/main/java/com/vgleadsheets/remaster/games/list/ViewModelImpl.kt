package com.vgleadsheets.remaster.games.list

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.model.Game
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ViewModelImpl @AssistedInject constructor(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    @Assisted private val navigateTo: (String) -> Unit,
) : VglsViewModel<State, Event>(
    initialState = State(persistentListOf())
) {
    init {
        repository.getAllGames()
            .onEach(::onGamesLoaded)
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun onGamesLoaded(games: List<Game>) {
        _uiState.update {
            it.copy(
                games = games
            )
        }
    }
}
