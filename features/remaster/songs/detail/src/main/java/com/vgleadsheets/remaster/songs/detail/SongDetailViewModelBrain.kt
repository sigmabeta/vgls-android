package com.vgleadsheets.remaster.songs.detail

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class SongDetailViewModelBrain(
    private val repository: VglsRepository,
    private val gameRepository: GameRepository,
    private val dispatchers: VglsDispatchers,
    private val coroutineScope: CoroutineScope,
    private val urlInfoProvider: UrlInfoProvider,
    private val stringProvider: StringProvider,
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
            is Action.SongThumbnailClicked -> onSongThumbnailClicked(action.id, action.pageNumber)
            is Action.GameClicked -> onGameClicked(action.id)
            is Action.ComposerClicked -> onComposerClicked(action.id)
            is Action.TagValueClicked -> onTagValueClicked(action.id)
        }
    }

    private fun startLoading(id: Long) {
        fetchUrlInfo()
        fetchSong(id)
        fetchComposers(id)
        fetchGame()
        fetchAliases(id)
        fetchTagValues(id)
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

    private fun fetchSong(id: Long) {
        repository
            .getSong(id)
            .onEach { song ->
                updateState {
                    (it as State).copy(song = song)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchComposers(songId: Long) {
        repository
            .getComposersForSong(songId)
            .onEach { composers ->
                updateState {
                    (it as State).copy(composers = composers)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchAliases(id: Long) {
        repository
            .getAliasesForSong(id)
            .onEach { songAliases ->
                updateState {
                    (it as State).copy(songAliases = songAliases)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchTagValues(id: Long) {
        repository
            .getTagValuesForSong(id)
            .onEach { tagValues ->
                updateState {
                    (it as State).copy(tagValues = tagValues)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun fetchGame() {
        internalUiState
            .map { (it as State).song?.gameId }
            .filterNotNull()
            .flatMapConcat { gameRepository.getGame(it) }
            .onEach { game ->
                updateState {
                    (it as State).copy(game = game)
                }
            }
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun onSongThumbnailClicked(id: Long, pageNumber: Int) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.SONG_VIEWER.forTwoArgs(id, pageNumber.toLong()),
                Destination.SONG_DETAIL.name
            )
        )
    }

    private fun onGameClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.GAME_DETAIL.forId(id),
                Destination.SONG_DETAIL.name
            )
        )
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.COMPOSER_DETAIL.forId(id),
                Destination.SONG_DETAIL.name
            )
        )
    }

    private fun onTagValueClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.TAGS_VALUES_SONG_LIST.forId(id),
                Destination.SONG_DETAIL.name
            )
        )
    }
}
