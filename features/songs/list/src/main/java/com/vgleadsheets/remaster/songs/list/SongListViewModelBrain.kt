package com.vgleadsheets.remaster.songs.list
import com.vgleadsheets.analytics.VglsAnalyticsScreen

import com.vgleadsheets.model.Song
import com.vgleadsheets.repository.SongRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.SageScheduler
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.nav.Destination
import net.sigmabeta.sage.ui.StringProvider

class SongListViewModelBrain(
    private val songRepository: SongRepository,
    private val scheduler: SageScheduler,
    private val analytics: Analytics,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = VglsAnalyticsScreen.LIST_SHEET

    override fun initialState() = State()

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> startLoading()
            is Action.SongClicked -> onSongClicked(action.id)
        }
    }

    private fun startLoading() {
        showLoading()
        collectSongs()
    }

    private fun collectSongs() {
        songRepository.getAllSongs()
            .onEach(::onSongsLoaded)
            .catch { error -> showError(error) }
            .runInBackground()
    }

    private fun showLoading() {
        updateSongs(LCE.Loading(LOAD_OPERATION_NAME))
    }

    private fun showError(error: Throwable) {
        updateSongs(LCE.Error(LOAD_OPERATION_NAME, error))
    }

    private fun onSongsLoaded(songs: List<Song>) {
        updateSongs(LCE.Content(songs))
    }

    private fun updateSongs(songs: LCE<List<Song>>) {
        updateState {
            (it as State).copy(
                songs = songs
            )
        }
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.SONGS_LIST.name
            )
        )
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "songs.list"
    }
}
