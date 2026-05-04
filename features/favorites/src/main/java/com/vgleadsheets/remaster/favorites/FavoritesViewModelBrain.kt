package com.vgleadsheets.remaster.favorites

import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.FavoriteRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.SageScheduler
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

class FavoritesViewModelBrain(
    private val favoriteRepository: FavoriteRepository,
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
    override val screenIdentifier = VglsAnalyticsScreen.LIST_FAVORITE

    override fun initialState() = State()

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitNoArgs -> collectFavorites()
            is Action.SongClicked -> onSongClicked(action.id)
            is Action.GameClicked -> onGameClicked(action.id)
            is Action.ComposerClicked -> onComposerClicked(action.id)
        }
    }

    private fun collectFavorites() {
        collectSongs()
        collectGames()
        collectComposers()
    }

    private fun collectSongs() {
        updateSongs(LCE.Loading(LOAD_OPERATION_SONGS))
        favoriteRepository.getAllSongs()
            .onEach(::onSongsLoaded)
            .catch { error -> updateSongs(LCE.Error(LOAD_OPERATION_SONGS, error)) }
            .runInBackground()
    }

    private fun collectGames() {
        updateGames(LCE.Loading(LOAD_OPERATION_GAMES))
        favoriteRepository.getAllGames()
            .onEach(::onGamesLoaded)
            .catch { error -> updateGames(LCE.Error(LOAD_OPERATION_GAMES, error)) }
            .runInBackground()
    }

    private fun collectComposers() {
        updateComposers(LCE.Loading(LOAD_OPERATION_COMPOSERS))
        favoriteRepository.getAllComposers()
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

    private fun onComposersLoaded(games: List<Composer>) {
        updateComposers(LCE.Content(games))
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.FAVORITES.name
            )
        )
    }

    private fun onGameClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.GAME_DETAIL.forId(id),
                Destination.FAVORITES.name
            )
        )
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.COMPOSER_DETAIL.forId(id),
                Destination.FAVORITES.name
            )
        )
    }

    private fun updateSongs(songs: LCE<List<Song>>) {
        updateState {
            (it as State).copy(
                favoriteSongs = songs
            )
        }
    }

    private fun updateGames(games: LCE<List<Game>>) {
        updateState {
            (it as State).copy(
                favoriteGames = games
            )
        }
    }

    private fun updateComposers(composers: LCE<List<Composer>>) {
        updateState {
            (it as State).copy(
                favoriteComposers = composers
            )
        }
    }

    companion object {
        private const val LOAD_OPERATION_SONGS = "favorites.songs"
        private const val LOAD_OPERATION_GAMES = "favorites.games"
        private const val LOAD_OPERATION_COMPOSERS = "favorites.composers"
    }
}
