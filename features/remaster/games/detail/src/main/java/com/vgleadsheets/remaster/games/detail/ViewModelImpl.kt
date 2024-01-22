package com.vgleadsheets.remaster.games.detail

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.coroutines.VglsDispatchers
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
    @Assisted gameId: Long,
) : VglsViewModel<State, Event>(
    initialState = State(persistentListOf())
) {
    init {
        repository.getGame(gameId)
            .onEach { game ->
                _uiState.update {
                    it.copy(
                        detailItems = persistentListOf(
                            NameCaptionListModel(
                                dataId = game.id,
                                name = game.name,
                                caption = "Is the name of the game",
                                onClick = { }
                            )
                        )
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }
}
