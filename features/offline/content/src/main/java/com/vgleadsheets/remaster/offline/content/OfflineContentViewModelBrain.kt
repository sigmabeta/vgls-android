package com.vgleadsheets.remaster.offline.content

import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.repository.OfflineRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.VglsScheduler
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.nav.Destination
import net.sigmabeta.sage.ui.StringProvider

class OfflineContentViewModelBrain(
    private val offlineRepository: OfflineRepository,
    private val scheduler: VglsScheduler,
    private val analytics: Analytics,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = AnalyticsScreen.LIST_OFFLINE

    override fun initialState() = State()

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> collectOfflineContent()
            is Action.SongClicked -> onSongClicked(action.id)
            is Action.GameClicked -> onGameClicked(action.id)
            is Action.ComposerClicked -> onComposerClicked(action.id)
        }
    }

    private fun collectOfflineContent() {
        collectSongs()
        collectGames()
        collectComposers()
    }

    private fun collectSongs() {
        updateSongs(LCE.Loading(LOAD_OPERATION_SONGS))
        offlineRepository.getAllSongs()
            .onEach(::onSongsLoaded)
            .catch { error -> updateSongs(LCE.Error(LOAD_OPERATION_SONGS, error)) }
            .runInBackground()
    }

    private fun collectGames() {
        updateGames(LCE.Loading(LOAD_OPERATION_GAMES))
        offlineRepository.getAllGames()
            .onEach(::onGamesLoaded)
            .catch { error -> updateGames(LCE.Error(LOAD_OPERATION_GAMES, error)) }
            .runInBackground()
    }

    private fun collectComposers() {
        updateComposers(LCE.Loading(LOAD_OPERATION_COMPOSERS))
        offlineRepository.getAllComposers()
            .onEach(::onComposersLoaded)
            .catch { error -> updateComposers(LCE.Error(LOAD_OPERATION_COMPOSERS, error)) }
            .runInBackground()
    }

    private fun onSongsLoaded(songs: List<Song>) {
        updateSongs(LCE.Content(songs))
    }

    private fun onGamesLoaded(games: List<Game>) {
        updateGames(LCE.Content(games))
    }

    private fun onComposersLoaded(composers: List<Composer>) {
        updateComposers(LCE.Content(composers))
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.OFFLINE.name
            )
        )
    }

    private fun onGameClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.GAME_DETAIL.forId(id),
                Destination.OFFLINE.name
            )
        )
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.COMPOSER_DETAIL.forId(id),
                Destination.OFFLINE.name
            )
        )
    }

    private fun updateSongs(songs: LCE<List<Song>>) {
        updateState {
            (it as State).copy(offlineSongs = songs)
        }
    }

    private fun updateGames(games: LCE<List<Game>>) {
        updateState {
            (it as State).copy(offlineGames = games)
        }
    }

    private fun updateComposers(composers: LCE<List<Composer>>) {
        updateState {
            (it as State).copy(offlineComposers = composers)
        }
    }

    companion object {
        private const val LOAD_OPERATION_SONGS = "offline.songs"
        private const val LOAD_OPERATION_GAMES = "offline.games"
        private const val LOAD_OPERATION_COMPOSERS = "offline.composers"
    }
}
