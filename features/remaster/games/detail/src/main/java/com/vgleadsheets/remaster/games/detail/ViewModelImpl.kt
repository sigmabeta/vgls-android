package com.vgleadsheets.remaster.games.detail

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.settings.environment.EnvironmentManager
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Named

class ViewModelImpl @AssistedInject constructor(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    @Named("EnvironmentManager") private val environmentManager: EnvironmentManager,
    @Assisted private val navigateTo: (String) -> Unit,
    @Assisted gameId: Long,
) : VglsViewModel<State, Event>(
    initialState = State()
) {
    init {
        fetchBaseImageUrl()
        fetchGame(gameId)
        fetchSongs(gameId)
        fetchComposers()
    }

    private fun fetchBaseImageUrl() {
        environmentManager
            .selectedEnvironmentFlow()
            .onEach { env ->
                _uiState.update {
                    it.copy(baseImageUrl = env.url)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun fetchGame(gameId: Long) {
        repository.getGame(gameId)
            .onEach { game ->
                _uiState.update {
                    it.copy(game = game)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun fetchSongs(gameId: Long) {
        repository
            .getSongsForGame(gameId)
            .onEach { songs ->
                _uiState.update {
                    it.copy(
                        songs = songs,
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun fetchComposers() {
        uiState
            .map { state -> state.songs }
            .mapList { song -> repository.getComposersForSongSync(song.id) }
            .map { it.flatten() }
            .map { it.distinct() }
            .onEach { composers ->
                _uiState.update {
                    it.copy(
                        composers = composers,
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }
}