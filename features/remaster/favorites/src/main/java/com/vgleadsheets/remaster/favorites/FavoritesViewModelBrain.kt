package com.vgleadsheets.remaster.favorites

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.onEach

class FavoritesViewModelBrain(
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
            is VglsAction.InitNoArgs -> collectFavorites()
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
        favoriteRepository.getAllSongs()
            .onEach(::onSongsLoaded)
            .runInBackground()
    }

    private fun collectGames() {
        favoriteRepository.getAllGames()
            .onEach(::onGamesLoaded)
            .runInBackground()
    }

    private fun collectComposers() {
        favoriteRepository.getAllComposers()
            .onEach(::onComposersLoaded)
            .runInBackground()
    }

    private fun onSongsLoaded(games: List<Song>) {
        updateState {
            (it as State).copy(
                favoriteSongs = games
            )
        }
    }

    private fun onGamesLoaded(games: List<Game>) {
        updateState {
            (it as State).copy(
                favoriteGames = games
            )
        }
    }

    private fun onComposersLoaded(games: List<Composer>) {
        updateState {
            (it as State).copy(
                favoriteComposers = games
            )
        }
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.FAVORITES.name
            )
        )
    }

    private fun onGameClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.GAME_DETAIL.forId(id),
                Destination.FAVORITES.name
            )
        )
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.COMPOSER_DETAIL.forId(id),
                Destination.FAVORITES.name
            )
        )
    }
}
