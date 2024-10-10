package com.vgleadsheets.remaster.songs.detail

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Composer
import com.vgleadsheets.model.Game
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.alias.SongAlias
import com.vgleadsheets.model.tag.TagValue
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.TagRepository
import com.vgleadsheets.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfo
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
            is Action.ToggleAltSelectedClicked -> onToggleAltSelectedClicked()
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
        checkAltSelectionStatus(id)
    }

    private fun fetchUrlInfo() {
        updateUrlInfo(LCE.Loading(LOAD_OPERATION_URL_INFO))
        urlInfoProvider
            .urlInfoFlow
            .onEach { urlInfo -> updateUrlInfo(LCE.Content(urlInfo)) }
            .catch { updateUrlInfo(LCE.Error(LOAD_OPERATION_URL_INFO, it)) }
            .runInBackground()
    }

    private fun fetchSong(id: Long) {
        updateSong(LCE.Loading(LOAD_OPERATION_SONG))
        songRepository
            .getSong(id)
            .onEach { song -> updateSong(LCE.Content(song)) }
            .catch { updateSong(LCE.Error(LOAD_OPERATION_SONG, it)) }
            .runInBackground()
    }

    private fun fetchComposers(songId: Long) {
        updateComposers(LCE.Loading(LOAD_OPERATION_COMPOSERS))
        composerRepository
            .getComposersForSong(songId)
            .onEach { composers -> updateComposers(LCE.Content(composers)) }
            .catch { updateComposers(LCE.Error(LOAD_OPERATION_COMPOSERS, it)) }
            .runInBackground()
    }

    private fun fetchAliases(id: Long) {
        updateAliases(LCE.Loading(LOAD_OPERATION_ALIASES))
        songRepository
            .getAliasesForSong(id)
            .onEach { songAliases -> updateAliases(LCE.Content(songAliases)) }
            .catch { updateAliases(LCE.Error(LOAD_OPERATION_ALIASES, it)) }
            .runInBackground()
    }

    private fun fetchTagValues(id: Long) {
        updateTagValues(LCE.Loading(LOAD_OPERATION_TAG_VALUES))
        tagRepository
            .getTagValuesForSong(id)
            .onEach { tagValues -> updateTagValues(LCE.Content(tagValues)) }
            .catch { updateTagValues(LCE.Error(LOAD_OPERATION_TAG_VALUES, it)) }
            .runInBackground()
    }

    private fun fetchGame() {
        updateGame(LCE.Loading(LOAD_OPERATION_GAME))
        internalUiState
            .map { (it as State).song }
            .mapNotNull { it as? LCE.Content }
            .flatMapConcat { gameRepository.getGame(it.data.gameId) }
            .onEach { game -> updateGame(LCE.Content(game)) }
            .catch { updateGame(LCE.Error(LOAD_OPERATION_GAME, it)) }
            .runInBackground()
    }

    private fun checkFavoriteStatus(id: Long) {
        updateIsFavorite(LCE.Loading(LOAD_OPERATION_IS_FAVORITE))
        favoriteRepository
            .isFavoriteSong(id)
            .onEach { isFavorite -> updateIsFavorite(LCE.Content(isFavorite)) }
            .catch { updateIsFavorite(LCE.Error(LOAD_OPERATION_IS_FAVORITE, it)) }
            .runInBackground()
    }

    private fun checkAltSelectionStatus(id: Long) {
        updateIsAltSelected(LCE.Loading(LOAD_OPERATION_IS_ALT_SELECTED))
        songRepository
            .isAlternateSelected(id)
            .onEach { isAltSelected -> updateIsAltSelected(LCE.Content(isAltSelected)) }
            .catch { updateIsAltSelected(LCE.Error(LOAD_OPERATION_IS_ALT_SELECTED, it)) }
            .runInBackground()
    }

    private fun onAddFavoriteClicked() {
        val state = internalUiState.value as State
        val song = state.song
        if (song !is LCE.Content) return

        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            favoriteRepository.addFavoriteSong(song.data.id)
        }
    }

    private fun onRemoveFavoriteClicked() {
        val state = internalUiState.value as State
        val song = state.song
        if (song !is LCE.Content) return

        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            favoriteRepository.removeFavoriteSong(song.data.id)
        }
    }

    private fun onToggleAltSelectedClicked() {
        val state = internalUiState.value as State
        val song = state.song
        if (song !is LCE.Content) return

        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            songRepository.toggleAlternate(song.data.id)
        }
    }

    private fun onSearchYoutubeClicked() {
        val state = internalUiState.value as State
        val song = state.song
        if (song !is LCE.Content) return
        val query = "${song.data.gameName} - ${song.data.name} Music"

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

    private fun updateSong(song: LCE<Song>) {
        updateState {
            (it as State).copy(
                song = song
            )
        }
    }

    private fun updateUrlInfo(urlInfo: LCE<UrlInfo>) {
        updateState {
            (it as State).copy(
                sheetUrlInfo = urlInfo
            )
        }
    }

    private fun updateGame(game: LCE<Game>) {
        updateState {
            (it as State).copy(
                game = game
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

    private fun updateAliases(alias: LCE<List<SongAlias>>) {
        updateState {
            (it as State).copy(
                songAliases = alias
            )
        }
    }

    private fun updateTagValues(tagValues: LCE<List<TagValue>>) {
        updateState {
            (it as State).copy(
                tagValues = tagValues
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

    private fun updateIsAltSelected(isAltSelected: LCE<Boolean>) {
        updateState {
            (it as State).copy(
                isAltSelected = isAltSelected
            )
        }
    }

    companion object {
        internal const val LOAD_OPERATION_SONG = "songs.detail"
        internal const val LOAD_OPERATION_URL_INFO = "songs.detail.urlinfo"
        internal const val LOAD_OPERATION_COMPOSERS = "songs.detail.composers"
        internal const val LOAD_OPERATION_GAME = "songs.detail.game"
        internal const val LOAD_OPERATION_ALIASES = "songs.detail.aliases"
        internal const val LOAD_OPERATION_TAG_VALUES = "songs.detail.tagvalues"
        internal const val LOAD_OPERATION_IS_FAVORITE = "songs.detail.favorite"
        internal const val LOAD_OPERATION_IS_ALT_SELECTED = "songs.detail.alternate"
    }
}
