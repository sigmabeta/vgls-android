package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class GameDetailViewModelBrain(
    private val repository: VglsRepository,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    private val urlInfoProvider: UrlInfoProvider,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    dispatchers,
    coroutineScope
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitWithId -> startLoading(action.id)
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
                updateState {
                    (it as State).copy(sheetUrlInfo = urlInfo)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchGame(gameId: Long) {
        repository.getGame(gameId)
            .onEach { game ->
                updateState {
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
                updateState {
                    (it as State).copy(
                        songs = songs,
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchComposers() {
        internalUiState
            .map { it as State }
            .map { state -> state.songs }
            .mapList { song -> repository.getComposersForSongSync(song.id) }
            .map { it.flatten() }
            .map { it.distinct() }
            .onEach { composers ->
                updateState {
                    (it as State).copy(
                        composers = composers,
                    )
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun onSongClicked(id: Long) {
        emitEvent(VglsEvent.NavigateTo(Destination.SONG_DETAIL.forId(id)))
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(VglsEvent.NavigateTo(Destination.COMPOSER_DETAIL.forId(id)))
    }
}
