package com.vgleadsheets.remaster.game

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class GameViewModel @AssistedInject constructor(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    @Assisted gameId: Long,
) : VglsViewModel<GameState, GameEvent>(
    initialState = GameState()
) {
    init {
        repository.getGame(gameId)
            .onEach { game ->
                _uiState.update {
                    it.copy(
                        title = game.name,
                        subtitle = "Is the name of the game"
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }
}
