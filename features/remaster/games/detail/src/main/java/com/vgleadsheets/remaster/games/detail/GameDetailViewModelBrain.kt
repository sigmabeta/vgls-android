package com.vgleadsheets.remaster.games.detail

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

class GameDetailViewModelBrain(
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
            is Action.ComposerClicked -> onComposerClicked(action.id)
        }
    }

    private fun startLoading(id: Long) {
        fetchUrlInfo()
        fetchGame(id)
        fetchSongs(id)
        fetchComposers()
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

    private fun fetchGame(gameId: Long) {
        repository.getGame(gameId)
            .onEach { game ->
                internalUiState.update {
                    (it as State).copy(
                        title = game.name,
                        game = game,
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchSongs(gameId: Long) {
        repository
            .getSongsForGame(gameId)
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

    private fun fetchComposers() {
        uiState
            .map { it as State }
            .map { state -> state.songs }
            .mapList { song -> repository.getComposersForSongSync(song.id) }
            .map { it.flatten() }
            .map { it.distinct() }
            .onEach { composers ->
                internalUiState.update {
                    (it as State).copy(
                        composers = composers,
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun onSongClicked(id: Long) {
        emitEvent(ListEvent.NavigateTo(Destination.SONG_VIEWER.forId(id)))
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(ListEvent.NavigateTo(Destination.COMPOSER_DETAIL.forId(id)))
    }
}
