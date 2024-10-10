package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class GameDetailViewModelBrain(
    private val songRepository: SongRepository,
    private val gameRepository: GameRepository,
    private val composerRepository: ComposerRepository,
    private val favoriteRepository: FavoriteRepository,
    private val scheduler: VglsScheduler,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitWithId -> startLoading(action.id)
            is Action.SongClicked -> onSongClicked(action.id)
            is Action.ComposerClicked -> onComposerClicked(action.id)
            is Action.AddFavoriteClicked -> onAddFavoriteClicked()
            is Action.RemoveFavoriteClicked -> onRemoveFavoriteClicked()
        }
    }

    private fun startLoading(id: Long) {
        fetchGame(id)
        fetchSongs(id)
        fetchComposers()
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
            .map { it as State }
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

    private fun checkFavoriteStatus(id: Long) {
        updateIsFavorite(LCE.Loading(LOAD_OPERATION_FAVORITE))
        favoriteRepository
            .isFavoriteGame(id)
            .onEach { isFavorite -> updateIsFavorite(LCE.Content(isFavorite)) }
            .catch { updateIsFavorite(LCE.Error(LOAD_OPERATION_FAVORITE, it)) }
            .runInBackground()
    }

    private fun onAddFavoriteClicked() {
        val state = internalUiState.value as State
        val game = state.game
        if (game !is LCE.Content) return

        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            favoriteRepository.addFavoriteGame(game.data.id)
        }
    }

    private fun onRemoveFavoriteClicked() {
        val state = internalUiState.value as State
        val game = state.game
        if (game !is LCE.Content) return

        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            favoriteRepository.removeFavoriteGame(game.data.id)
        }
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.GAME_DETAIL.name
            )
        )
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.COMPOSER_DETAIL.forId(id),
                Destination.GAME_DETAIL.name
            )
        )
    }

    private fun updateGame(game: LCE<Game>) {
        updateState {
            (it as State).copy(
                game = game
            )
        }
    }

    private fun updateSongs(songs: LCE<List<Song>>) {
        updateState {
            (it as State).copy(
                songs = songs
            )
        }
    }

    private fun updateComposers(composers: LCE<List<Composer>>) {
        updateState {
            (it as State).copy(
                composers = composers
            )
        }
    }

    private fun updateIsFavorite(isFavorite: LCE<Boolean>) {
        updateState {
            (it as State).copy(
                isFavorite = isFavorite
            )
        }
    }

    companion object {
        internal const val LOAD_OPERATION_GAME = "games.detail"
        internal const val LOAD_OPERATION_SONGS = "games.detail.songs"
        internal const val LOAD_OPERATION_COMPOSERS = "games.detail.composers"
        internal const val LOAD_OPERATION_FAVORITE = "games.detail.favorite"
    }
}
