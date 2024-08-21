package com.vgleadsheets.remaster.composers.detail

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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take

class ComposerDetailViewModelBrain(
    private val songRepository: SongRepository,
    private val composerRepository: ComposerRepository,
    private val gameRepository: GameRepository,
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
            is Action.GameClicked -> onGameClicked(action.id)
            is Action.AddFavoriteClicked -> onAddFavoriteClicked()
            is Action.RemoveFavoriteClicked -> onRemoveFavoriteClicked()
        }
    }

    private fun startLoading(id: Long) {
        fetchComposer(id)
        fetchSongs(id)
        fetchGames()
        checkFavoriteStatus(id)
    }

    private fun checkFavoriteStatus(id: Long) {
        favoriteRepository
            .isFavoriteComposer(id)
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
            .mapNotNull { it.composer?.id }
            .take(1)
            .onEach { id ->
                favoriteRepository.addFavoriteComposer(id)
            }
            .runInBackground()
    }

    private fun onRemoveFavoriteClicked() {
        internalUiState
            .map { it as State }
            .mapNotNull { it.composer?.id }
            .take(1)
            .onEach { id ->
                favoriteRepository.removeFavoriteComposer(id)
            }
            .runInBackground()
    }

    private fun fetchComposer(composerId: Long) {
        composerRepository.getComposer(composerId)
            .onEach { composer ->
                updateState {
                    (it as State).copy(
                        title = composer.name,
                        composer = composer,
                    )
                }
            }
            .runInBackground()
    }

    private fun fetchSongs(composerId: Long) {
        songRepository
            .getSongsForComposer(composerId)
            .onEach { songs ->
                updateState {
                    (it as State).copy(
                        songs = songs,
                    )
                }
            }
            .runInBackground()
    }

    private fun fetchGames() {
        internalUiState
            .map { it as State }
            .map { state -> state.songs }
            .mapList { song -> gameRepository.getGameSync(song.gameId) }
            .map { it.distinct() }
            .onEach { games ->
                updateState {
                    (it as State).copy(
                        games = games,
                    )
                }
            }
            .runInBackground()
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
}
