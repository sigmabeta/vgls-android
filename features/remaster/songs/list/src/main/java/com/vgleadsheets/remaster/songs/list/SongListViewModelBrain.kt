package com.vgleadsheets.remaster.songs.list

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SongListViewModelBrain(
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
            is VglsAction.InitNoArgs -> startLoading()
            is Action.SongClicked -> onSongClicked(action.id)
        }
    }

    private fun startLoading() {
        fetchUrlInfo()
        collectSongs()
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

    private fun collectSongs() {
        repository.getAllSongs()
            .onEach(::onSongsLoaded)
            .flowOn(dispatchers.disk)
            .launchIn(coroutineScope)
    }

    private fun onSongsLoaded(songs: List<Song>) {
        updateState {
            (it as State).copy(
                songs = songs
            )
        }
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.SONGS_LIST.name
            )
        )
    }
}
