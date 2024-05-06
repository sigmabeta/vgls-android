package com.vgleadsheets.remaster.composers.detail

import androidx.lifecycle.viewModelScope
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.urlinfo.UrlInfoProvider
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ViewModelImpl @AssistedInject constructor(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    private val urlInfoProvider: UrlInfoProvider,
    @Assisted private val navigateTo: (String) -> Unit,
    @Assisted composerId: Long,
) : VglsViewModel<State, Event>(
    initialState = State()
) {
    init {
        fetchUrlInfo()
        fetchComposer(composerId)
        fetchSongs(composerId)
//        fetchGames()
    }

    private fun fetchUrlInfo() {
        urlInfoProvider
            .urlInfoFlow
            .onEach { urlInfo ->
                _uiState.update {
                    it.copy(sheetUrlInfo = urlInfo)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun fetchComposer(composerId: Long) {
        repository.getComposer(composerId)
            .onEach { composer ->
                _uiState.update {
                    it.copy(composer = composer)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(viewModelScope)
    }

    private fun fetchSongs(composerId: Long) {
        repository
            .getSongsForComposer(composerId)
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

//    private fun fetchGames() {
//        uiState
//            .map { state -> state.songs }
//            .mapList { song -> repository.getGamesForComposerSync(song.id) }
//            .map { it.flatten() }
//            .map { it.distinct() }
//            .onEach { games ->
//                _uiState.update {
//                    it.copy(
//                        games = games,
//                    )
//                }
//            }
//            .flowOn(dispatchers.disk)
//            .launchIn(viewModelScope)
//    }
}
