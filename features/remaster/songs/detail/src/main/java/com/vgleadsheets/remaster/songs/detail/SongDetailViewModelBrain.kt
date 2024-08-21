package com.vgleadsheets.remaster.songs.detail

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
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take

class SongDetailViewModelBrain(
    private val songRepository: SongRepository,
    private val gameRepository: GameRepository,
    private val composerRepository: ComposerRepository,
    private val favoriteRepository: FavoriteRepository,
    private val tagRepository: TagRepository,
    private val scheduler: VglsScheduler,
    private val urlInfoProvider: UrlInfoProvider,
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
            is Action.SongThumbnailClicked -> onSongThumbnailClicked(action.id, action.pageNumber)
            is Action.GameClicked -> onGameClicked(action.id)
            is Action.ComposerClicked -> onComposerClicked(action.id)
            is Action.TagValueClicked -> onTagValueClicked(action.id)
            is Action.AddFavoriteClicked -> onAddFavoriteClicked()
            is Action.RemoveFavoriteClicked -> onRemoveFavoriteClicked()
            is Action.SearchYoutubeClicked -> onSearchYoutubeClicked()
        }
    }

    private fun startLoading(id: Long) {
        fetchUrlInfo()
        fetchSong(id)
        fetchComposers(id)
        fetchGame()
        fetchAliases(id)
        fetchTagValues(id)
        checkFavoriteStatus(id)
    }

    private fun fetchUrlInfo() {
        urlInfoProvider
            .urlInfoFlow
            .onEach { urlInfo ->
                updateState {
                    (it as State).copy(sheetUrlInfo = urlInfo)
                }
            }
            .runInBackground()
    }

    private fun fetchSong(id: Long) {
        songRepository
            .getSong(id)
            .onEach { song ->
                updateState {
                    (it as State).copy(song = song)
                }
            }
            .runInBackground()
    }

    private fun fetchComposers(songId: Long) {
        composerRepository
            .getComposersForSong(songId)
            .onEach { composers ->
                updateState {
                    (it as State).copy(composers = composers)
                }
            }
            .runInBackground()
    }

    private fun fetchAliases(id: Long) {
        songRepository
            .getAliasesForSong(id)
            .onEach { songAliases ->
                updateState {
                    (it as State).copy(songAliases = songAliases)
                }
            }
            .runInBackground()
    }

    private fun fetchTagValues(id: Long) {
        tagRepository
            .getTagValuesForSong(id)
            .onEach { tagValues ->
                updateState {
                    (it as State).copy(tagValues = tagValues)
                }
            }
            .runInBackground()
    }

    private fun fetchGame() {
        internalUiState
            .map { (it as State).song?.gameId }
            .filterNotNull()
            .flatMapConcat { gameRepository.getGame(it) }
            .onEach { game ->
                updateState {
                    (it as State).copy(game = game)
                }
            }
            .runInBackground()
    }

    private fun checkFavoriteStatus(id: Long) {
        favoriteRepository
            .isFavoriteSong(id)
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
            .mapNotNull { it.song?.id }
            .take(1)
            .onEach { id ->
                favoriteRepository.addFavoriteSong(id)
            }
            .runInBackground()
    }

    private fun onRemoveFavoriteClicked() {
        internalUiState
            .map { it as State }
            .mapNotNull { it.song?.id }
            .take(1)
            .onEach { id ->
                favoriteRepository.removeFavoriteSong(id)
            }
            .runInBackground()
    }

    private fun onSearchYoutubeClicked() {
        val state = internalUiState.value as State
        val song = state.song ?: return
        val query = "${song.gameName} - ${song.name} Music"

        emitEvent(
            VglsEvent.SearchYoutubeClicked(query)
        )
    }

    private fun onSongThumbnailClicked(id: Long, pageNumber: Int) {
        navigateTo(Destination.SONG_VIEWER.forTwoArgs(id, pageNumber.toLong()))
    }

    private fun onGameClicked(id: Long) {
        navigateTo(Destination.GAME_DETAIL.forId(id))
    }

    private fun onComposerClicked(id: Long) {
        navigateTo(Destination.COMPOSER_DETAIL.forId(id))
    }

    private fun onTagValueClicked(id: Long) {
        navigateTo(Destination.TAGS_VALUES_SONG_LIST.forId(id))
    }

    private fun navigateTo(destination: String) {
        emitEvent(
            VglsEvent.NavigateTo(
                destination,
                Destination.SONG_DETAIL.name
            )
        )
    }
}
