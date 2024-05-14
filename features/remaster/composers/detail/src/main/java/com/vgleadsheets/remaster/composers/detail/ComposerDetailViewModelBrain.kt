package com.vgleadsheets.remaster.composers.detail

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListAction
import com.vgleadsheets.list.ListEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ComposerDetailViewModelBrain(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    private val urlInfoProvider: UrlInfoProvider,
) : ListViewModelBrain() {
    override fun initialState() = State()

    override fun handleAction(action: ListAction) {
        when (action) {
            is ListAction.InitWithId -> startLoading(action.id)
            is Action.SongClicked -> onSongClicked(action.id)
            is Action.GameClicked -> onGameClicked(action.id)
        }
    }

    private fun startLoading(id: Long) {
        fetchUrlInfo()
        fetchComposer(id)
        fetchSongs(id)
        fetchGames()
    }

    private fun fetchUrlInfo() {
        urlInfoProvider
            .urlInfoFlow
            .onEach { urlInfo ->
                internalUiState.update {
                    (it as State).copy(sheetUrlInfo = urlInfo)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchComposer(composerId: Long) {
        repository.getComposer(composerId)
            .onEach { composer ->
                internalUiState.update {
                    (it as State).copy(
                        title = composer.name,
                        composer = composer,
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchSongs(composerId: Long) {
        repository
            .getSongsForComposer(composerId)
            .onEach { songs ->
                internalUiState.update {
                    (it as State).copy(
                        songs = songs,
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchGames() {
        uiState
            .map { it as State }
            .map { state -> state.songs }
            .mapList { song -> repository.getGameSync(song.gameId) }
            .map { it.distinct() }
            .onEach { games ->
                internalUiState.update {
                    (it as State).copy(
                        games = games,
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun onSongClicked(id: Long) {
        emitEvent(ListEvent.NavigateTo(Destination.SONG_VIEWER.forId(id)))
    }

    private fun onGameClicked(id: Long) {
        emitEvent(ListEvent.NavigateTo(Destination.GAME_DETAIL.forId(id)))
    }
}
