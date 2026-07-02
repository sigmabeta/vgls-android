package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.analytics.VglsAnalytics
import com.vgleadsheets.analytics.VglsAnalyticsScreen
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.viewmodel.list.VglsListViewModel
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ManualViewModelAssistedFactoryKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

@AssistedInject
class GameDetailViewModel(
    @Assisted private val idArg: Long,
    override val stringProvider: StringProvider,
    override val analytics: VglsAnalytics,
    override val hatchet: Hatchet,
    override val dispatchers: SageDispatchers,
    override val delayManager: DelayManager,
    override val eventDispatcher: EventDispatcher,
    override val showDebugProvider: ShowDebugProvider,
    private val songRepository: SongRepository,
    private val gameRepository: GameRepository,
    private val composerRepository: ComposerRepository,
    private val favoriteRepository: FavoriteRepository,
    private val offlineRepository: OfflineRepository,
) : VglsListViewModel<State>() {
    override val screenIdentifier = VglsAnalyticsScreen.DETAIL_GAME

    override fun initialState() = State()

    init {
        sendAction(SageAction.InitWithId(idArg))
    }

    override fun handleAction(action: SageAction) {
        when (action) {
            is SageAction.InitWithId -> startLoading(action.id)
            is Action.SongClicked -> onSongClicked(action.id)
            is Action.ComposerClicked -> onComposerClicked(action.id)
            is Action.AddFavoriteClicked -> onAddFavoriteClicked()
            is Action.RemoveFavoriteClicked -> onRemoveFavoriteClicked()
            is Action.EnableOfflineClicked -> onEnableOfflineClicked()
            is Action.DisableOfflineClicked -> onDisableOfflineClicked()
        }
    }

    private fun startLoading(id: Long) {
        fetchGame(id)
        fetchSongs(id)
        fetchComposers()
        checkOfflineStatus(id)
        checkFavoriteStatus(id)
    }

    private fun fetchGame(gameId: Long) {
        updateGame(LCE.Loading(LOAD_OPERATION_GAME))
        gameRepository.getGame(gameId)
            .onEach { game -> updateGame(LCE.Content(game)) }
            .catch { updateGame(LCE.Error(LOAD_OPERATION_GAME, it)) }
            .runInBackground()
    }

    private fun fetchSongs(gameId: Long) {
        updateSongs(LCE.Loading(LOAD_OPERATION_SONGS))
        songRepository
            .getSongsForGame(gameId)
            .onEach { songs -> updateSongs(LCE.Content(songs)) }
            .catch { updateSongs(LCE.Error(LOAD_OPERATION_SONGS, it)) }
            .runInBackground()
    }

    private fun fetchComposers() {
        updateComposers(LCE.Loading(LOAD_OPERATION_COMPOSERS))
        internalUiState
            .map { state -> state.songs }
            .map { songs -> songLCEtoComposerLCE(songs) }
            .catch { updateComposers(LCE.Error(LOAD_OPERATION_COMPOSERS, it)) }
            .onEach { composersLCE -> updateComposers(composersLCE) }
            .runInBackground()
    }

    private suspend fun songLCEtoComposerLCE(songs: LCE<List<Song>>) = when (songs) {
        is LCE.Content -> LCE.Content(getUniqueComposers(songs.data))
        is LCE.Error -> LCE.Uninitialized
        is LCE.Loading -> LCE.Loading(LOAD_OPERATION_COMPOSERS)
        LCE.Uninitialized -> LCE.Uninitialized
    }

    private suspend fun getUniqueComposers(songs: List<Song>): List<Composer> {
        val composerLists = songs.map { song ->
            composerRepository
                .getComposersForSong(song.id)
                .firstOrNull() ?: emptyList()
        }

        return composerLists.flatten().distinct()
    }

    private fun checkOfflineStatus(id: Long) {
        updateIsAvailableOffline(LCE.Loading(LOAD_OPERATION_IS_OFFLINE))
        offlineRepository
            .isOfflineGame(id)
            .onEach { isOffline -> updateIsAvailableOffline(LCE.Content(isOffline)) }
            .catch { updateIsAvailableOffline(LCE.Error(LOAD_OPERATION_IS_OFFLINE, it)) }
            .runInBackground()
    }

    private fun checkFavoriteStatus(id: Long) {
        updateIsFavorite(LCE.Loading(LOAD_OPERATION_IS_FAVORITE))
        favoriteRepository
            .isFavoriteGame(id)
            .onEach { isFavorite -> updateIsFavorite(LCE.Content(isFavorite)) }
            .catch { updateIsFavorite(LCE.Error(LOAD_OPERATION_IS_FAVORITE, it)) }
            .runInBackground()
    }

    private fun onAddFavoriteClicked() {
        val state = internalUiState.value
        val game = state.game
        if (game !is LCE.Content) return

        updateIsFavorite(LCE.Loading(LOAD_OPERATION_IS_FAVORITE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            favoriteRepository.addFavoriteGame(game.data.id)
        }
    }

    private fun onRemoveFavoriteClicked() {
        val state = internalUiState.value
        val game = state.game
        if (game !is LCE.Content) return

        updateIsFavorite(LCE.Loading(LOAD_OPERATION_IS_FAVORITE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            favoriteRepository.removeFavoriteGame(game.data.id)
        }
    }

    private fun onEnableOfflineClicked() {
        val state = internalUiState.value
        val game = state.game
        if (game !is LCE.Content) return

        updateIsAvailableOffline(LCE.Loading(LOAD_OPERATION_IS_OFFLINE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            offlineRepository.addOfflineGame(game.data.id)
        }
    }

    private fun onDisableOfflineClicked() {
        val state = internalUiState.value
        val game = state.game
        if (game !is LCE.Content) return

        updateIsAvailableOffline(LCE.Loading(LOAD_OPERATION_IS_OFFLINE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            offlineRepository.removeOfflineGame(game.data.id)
        }
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.GAME_DETAIL.name
            )
        )
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(
            SageEvent.NavigateTo(
                Destination.COMPOSER_DETAIL.forId(id),
                Destination.GAME_DETAIL.name
            )
        )
    }

    private fun updateGame(game: LCE<Game>) {
        if (game is LCE.Content) {
            val gameData = game.data

            analytics.logGameView(
                gameName = gameData.name,
            )
        }

        updateState {
            it.copy(
                game = game
            )
        }
    }

    private fun updateIsAvailableOffline(isAvailableOffline: LCE<Boolean>) {
        updateState {
            it.copy(
                isAvailableOffline = isAvailableOffline
            )
        }
    }

    private fun updateSongs(songs: LCE<List<Song>>) {
        updateState {
            it.copy(
                songs = songs
            )
        }
    }

    private fun updateComposers(composers: LCE<List<Composer>>) {
        updateState {
            it.copy(
                composers = composers
            )
        }
    }

    private fun updateIsFavorite(isFavorite: LCE<Boolean>) {
        updateState {
            it.copy(
                isFavorite = isFavorite
            )
        }
    }

    @AssistedFactory
    @ManualViewModelAssistedFactoryKey(Factory::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ManualViewModelAssistedFactory {
        fun create(@Assisted idArg: Long): GameDetailViewModel
    }

    companion object {
        internal const val LOAD_OPERATION_GAME = "games.detail"
        internal const val LOAD_OPERATION_SONGS = "games.detail.songs"
        internal const val LOAD_OPERATION_COMPOSERS = "games.detail.composers"
        internal const val LOAD_OPERATION_IS_FAVORITE = "games.detail.favorite"
        internal const val LOAD_OPERATION_IS_OFFLINE = "games.detail.offline"
    }
}
