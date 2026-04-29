package com.vgleadsheets.remaster.composers.detail

import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreen
import net.sigmabeta.sage.appcomm.LCE
import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import net.sigmabeta.sage.logging.Hatchet
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import net.sigmabeta.sage.nav.Destination
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.repository.SongRepository
import net.sigmabeta.sage.ui.StringProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ComposerDetailViewModelBrain(
    private val songRepository: SongRepository,
    private val composerRepository: ComposerRepository,
    private val gameRepository: GameRepository,
    private val favoriteRepository: FavoriteRepository,
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
    override val screenIdentifier = AnalyticsScreen.DETAIL_COMPOSER

    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitWithId -> startLoading(action.id)
            is Action.SongClicked -> onSongClicked(action.id)
            is Action.GameClicked -> onGameClicked(action.id)
            is Action.AddFavoriteClicked -> onAddFavoriteClicked()
            is Action.RemoveFavoriteClicked -> onRemoveFavoriteClicked()
            is Action.EnableOfflineClicked -> onEnableOfflineClicked()
            is Action.DisableOfflineClicked -> onDisableOfflineClicked()
        }
    }

    private fun startLoading(id: Long) {
        fetchComposer(id)
        fetchSongs(id)
        fetchGames()
        checkOfflineStatus(id)
        checkFavoriteStatus(id)
    }

    private fun fetchComposer(composerId: Long) {
        updateComposer(LCE.Loading(LOAD_OPERATION_COMPOSER))
        composerRepository.getComposer(composerId)
            .onEach { composer -> updateComposer(LCE.Content(composer)) }
            .catch { updateComposer(LCE.Error(LOAD_OPERATION_COMPOSER, it)) }
            .runInBackground()
    }

    private fun fetchSongs(composerId: Long) {
        updateSongs(LCE.Loading(LOAD_OPERATION_SONGS))
        songRepository
            .getSongsForComposer(composerId)
            .onEach { songs -> updateSongs(LCE.Content(songs)) }
            .catch { updateSongs(LCE.Error(LOAD_OPERATION_SONGS, it)) }
            .runInBackground()
    }

    private fun fetchGames() {
        updateGames(LCE.Loading(LOAD_OPERATION_GAMES))
        internalUiState
            .map { it as State }
            .map { state -> state.songs }
            .map { songs -> songLCEtoGameLCE(songs) }
            .catch { updateGames(LCE.Error(LOAD_OPERATION_GAMES, it)) }
            .onEach { gamesLCE -> updateGames(gamesLCE) }
            .runInBackground()
    }

    private suspend fun songLCEtoGameLCE(songs: LCE<List<Song>>) = when (songs) {
        is LCE.Content -> LCE.Content(getUniqueGames(songs.data))
        is LCE.Error -> LCE.Uninitialized
        is LCE.Loading -> LCE.Loading(LOAD_OPERATION_GAMES)
        LCE.Uninitialized -> LCE.Uninitialized
    }

    private suspend fun getUniqueGames(songs: List<Song>): List<Game> {
        val games = songs
            .mapNotNull { song ->
                gameRepository
                    .getGame(song.gameId)
                    .firstOrNull()
            }
            .distinctBy { it.id }

        return games
    }

    private fun checkOfflineStatus(id: Long) {
        updateIsAvailableOffline(LCE.Loading(LOAD_OPERATION_IS_OFFLINE))
        offlineRepository
            .isOfflineComposer(id)
            .onEach { isOffline -> updateIsAvailableOffline(LCE.Content(isOffline)) }
            .catch { updateIsAvailableOffline(LCE.Error(LOAD_OPERATION_IS_OFFLINE, it)) }
            .runInBackground()
    }

    private fun checkFavoriteStatus(id: Long) {
        updateIsFavorite(LCE.Loading(LOAD_OPERATION_IS_FAVORITE))
        favoriteRepository
            .isFavoriteComposer(id)
            .onEach { isFavorite -> updateIsFavorite(LCE.Content(isFavorite)) }
            .catch { updateIsFavorite(LCE.Error(LOAD_OPERATION_IS_FAVORITE, it)) }
            .runInBackground()
    }

    private fun onAddFavoriteClicked() {
        val state = internalUiState.value as State
        val composer = state.composer
        if (composer !is LCE.Content) return

        updateIsFavorite(LCE.Loading(LOAD_OPERATION_IS_FAVORITE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            favoriteRepository.addFavoriteComposer(composer.data.id)
        }
    }

    private fun onRemoveFavoriteClicked() {
        val state = internalUiState.value as State
        val composer = state.composer
        if (composer !is LCE.Content) return

        updateIsFavorite(LCE.Loading(LOAD_OPERATION_IS_FAVORITE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            favoriteRepository.removeFavoriteComposer(composer.data.id)
        }
    }

    private fun onEnableOfflineClicked() {
        val state = internalUiState.value as State
        val composer = state.composer
        if (composer !is LCE.Content) return

        updateIsAvailableOffline(LCE.Loading(LOAD_OPERATION_IS_OFFLINE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            offlineRepository.addOfflineComposer(composer.data.id)
        }
    }

    private fun onDisableOfflineClicked() {
        val state = internalUiState.value as State
        val composer = state.composer
        if (composer !is LCE.Content) return

        updateIsAvailableOffline(LCE.Loading(LOAD_OPERATION_IS_OFFLINE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            offlineRepository.removeOfflineComposer(composer.data.id)
        }
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.COMPOSER_DETAIL.name
            )
        )
    }

    private fun onGameClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.GAME_DETAIL.forId(id),
                Destination.COMPOSER_DETAIL.name
            )
        )
    }

    private fun updateComposer(composer: LCE<Composer>) {
        if (composer is LCE.Content) {
            val composerData = composer.data

            analytics.logComposerView(
                composerName = composerData.name,
            )
        }

        updateState {
            (it as State).copy(
                composer = composer,
            )
        }
    }

    private fun updateSongs(songs: LCE<List<Song>>) {
        updateState {
            (it as State).copy(
                songs = songs,
            )
        }
    }

    private fun updateGames(games: LCE<List<Game>>) {
        updateState {
            (it as State).copy(
                games = games,
            )
        }
    }

    private fun updateIsAvailableOffline(isAvailableOffline: LCE<Boolean>) {
        updateState {
            (it as State).copy(
                isAvailableOffline = isAvailableOffline
            )
        }
    }

    private fun updateIsFavorite(isFavorite: LCE<Boolean>) {
        updateState {
            (it as State).copy(
                isFavorite = isFavorite,
            )
        }
    }

    companion object {
        internal const val LOAD_OPERATION_COMPOSER = "composers.detail"
        internal const val LOAD_OPERATION_SONGS = "composers.detail.songs"
        internal const val LOAD_OPERATION_GAMES = "composers.detail.games"
        internal const val LOAD_OPERATION_IS_FAVORITE = "composers.detail.favorite"
        internal const val LOAD_OPERATION_IS_OFFLINE = "composers.detail.offline"
    }
}
