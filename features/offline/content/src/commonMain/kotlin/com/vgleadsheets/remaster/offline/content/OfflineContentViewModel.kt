package com.vgleadsheets.remaster.offline.content

import androidx.lifecycle.ViewModel
import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.di.AppScope
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

@ContributesIntoMap(AppScope::class, binding = binding<ViewModel>())
@ViewModelKey
class OfflineContentViewModel @Inject constructor(
    override val stringProvider: StringProvider,
    override val analytics: Analytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val offlineRepository: OfflineRepository,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.LIST_OFFLINE

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitNoArgs)
    }

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
            it.copy(offlineSongs = songs)
        }
    }

    private fun updateGames(games: LCE<List<Game>>) {
        updateState {
            it.copy(offlineGames = games)
        }
    }

    private fun updateComposers(composers: LCE<List<Composer>>) {
        updateState {
            it.copy(offlineComposers = composers)
        }
    }

    companion object {
        private const val LOAD_OPERATION_SONGS = "offline.songs"
        private const val LOAD_OPERATION_GAMES = "offline.games"
        private const val LOAD_OPERATION_COMPOSERS = "offline.composers"
    }
}
