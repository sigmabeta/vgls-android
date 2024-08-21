package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take

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
        gameRepository.getGame(gameId)
            .onEach { game ->
                updateState {
                    (it as State).copy(
                        game = game,
                    )
                }
            }
            .runInBackground()
    }

    private fun fetchSongs(gameId: Long) {
        songRepository
            .getSongsForGame(gameId)
            .onEach { songs ->
                updateState {
                    (it as State).copy(
                        songs = songs,
                    )
                }
            }
            .runInBackground()
    }

    private fun fetchComposers() {
        internalUiState
            .map { it as State }
            .map { state -> state.songs }
            .map { songs ->
                songs.map { song ->
                    composerRepository.getComposersForSong(song.id).firstOrNull() ?: emptyList()
                }
            }
            .map { it.flatten() }
            .map { it.distinct() }
            .onEach { composers ->
                updateState {
                    (it as State).copy(
                        composers = composers,
                    )
                }
            }
            .runInBackground()
    }

    private fun checkFavoriteStatus(id: Long) {
        favoriteRepository
            .isFavoriteGame(id)
            .onEach { isFavorite ->
                updateState {
                    (it as State).copy(isFavorite = isFavorite)
                }
            }
            .runInBackground()
    }

    private fun onAddFavoriteClicked() {
        internalUiState
            .map { it as State }
            .mapNotNull { it.game?.id }
            .take(1)
            .onEach { id ->
                favoriteRepository.addFavoriteGame(id)
            }
            .runInBackground()
    }

    private fun onRemoveFavoriteClicked() {
        internalUiState
            .map { it as State }
            .mapNotNull { it.game?.id }
            .take(1)
            .onEach { id ->
                favoriteRepository.removeFavoriteGame(id)
            }
            .runInBackground()
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
}
