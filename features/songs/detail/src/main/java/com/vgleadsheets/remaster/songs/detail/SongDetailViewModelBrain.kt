package com.vgleadsheets.remaster.songs.detail

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
import com.vgleadsheets.model.alias.SongAlias
import com.vgleadsheets.model.tag.TagValue
import net.sigmabeta.sage.nav.Destination
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.repository.FavoriteRepository
import com.vgleadsheets.repository.GameRepository
import com.vgleadsheets.repository.OfflineRepository
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.repository.TagRepository
import net.sigmabeta.sage.ui.StringProvider
import com.vgleadsheets.urlinfo.UrlInfo
import com.vgleadsheets.urlinfo.UrlInfoProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class SongDetailViewModelBrain(
    private val songRepository: SongRepository,
    private val gameRepository: GameRepository,
    private val composerRepository: ComposerRepository,
    private val favoriteRepository: FavoriteRepository,
    private val offlineRepository: OfflineRepository,
    private val tagRepository: TagRepository,
    private val scheduler: VglsScheduler,
    private val urlInfoProvider: UrlInfoProvider,
    private val analytics: Analytics,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    analytics,
    hatchet,
    scheduler,
) {
    override val screenIdentifier = AnalyticsScreen.DETAIL_SHEET

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
            is Action.EnableOfflineClicked -> onEnableOfflineClicked()
            is Action.DisableOfflineClicked -> onDisableOfflineClicked()
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
        checkOfflineStatus(id)
        checkFavoriteStatus(id)
        checkAltSelectionStatus(id)
        setupAnalytics()
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

    private fun checkOfflineStatus(id: Long) {
        updateIsAvailableOffline(LCE.Loading(LOAD_OPERATION_IS_OFFLINE))
        offlineRepository
            .isOfflineSong(id)
            .onEach { isOffline -> updateIsAvailableOffline(LCE.Content(isOffline)) }
            .catch { updateIsAvailableOffline(LCE.Error(LOAD_OPERATION_IS_OFFLINE, it)) }
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

    private fun setupAnalytics() {
        internalUiState
            .map { it as State }
            .filter { it.song is LCE.Content && it.sheetUrlInfo is LCE.Content }
            .take(1)
            .onEach(::reportSongView)
            .flowOn(scheduler.dispatchers.network)
            .launchIn(scheduler.coroutineScope)
    }

    fun reportSongView(state: State) {
        val song = state.song
        if (song is LCE.Content) {
            val songData = song.data

            val partId = state.sheetUrlInfo.getPart()

            analytics.logSongView(
                id = songData.id,
                songName = songData.name,
                gameName = songData.gameName,
                transposition = partId,
            )
        }
    }

    private fun onAddFavoriteClicked() {
        val state = internalUiState.value as State
        val song = state.song
        if (song !is LCE.Content) return

        updateIsFavorite(LCE.Loading(LOAD_OPERATION_IS_FAVORITE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            favoriteRepository.addFavoriteSong(song.data.id)
        }
    }

    private fun onRemoveFavoriteClicked() {
        val state = internalUiState.value as State
        val song = state.song
        if (song !is LCE.Content) return

        updateIsFavorite(LCE.Loading(LOAD_OPERATION_IS_FAVORITE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            favoriteRepository.removeFavoriteSong(song.data.id)
        }
    }

    private fun onEnableOfflineClicked() {
        val state = internalUiState.value as State
        val song = state.song
        if (song !is LCE.Content) return

        updateIsAvailableOffline(LCE.Loading(LOAD_OPERATION_IS_OFFLINE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            offlineRepository.addOfflineSong(song.data.id)
        }
    }

    private fun onDisableOfflineClicked() {
        val state = internalUiState.value as State
        val song = state.song
        if (song !is LCE.Content) return

        updateIsAvailableOffline(LCE.Loading(LOAD_OPERATION_IS_OFFLINE))
        scheduler.coroutineScope.launch(scheduler.dispatchers.disk) {
            offlineRepository.removeOfflineSong(song.data.id)
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

    private fun LCE<UrlInfo>.getPart() = if (this is LCE.Content) {
        data.partId
    } else {
        null
    }

    companion object {
        internal const val LOAD_OPERATION_SONG = "songs.detail"
        internal const val LOAD_OPERATION_URL_INFO = "songs.detail.urlinfo"
        internal const val LOAD_OPERATION_COMPOSERS = "songs.detail.composers"
        internal const val LOAD_OPERATION_GAME = "songs.detail.game"
        internal const val LOAD_OPERATION_ALIASES = "songs.detail.aliases"
        internal const val LOAD_OPERATION_TAG_VALUES = "songs.detail.tagvalues"
        internal const val LOAD_OPERATION_IS_FAVORITE = "songs.detail.favorite"
        internal const val LOAD_OPERATION_IS_OFFLINE = "songs.detail.offline"
        internal const val LOAD_OPERATION_IS_ALT_SELECTED = "songs.detail.alternate"
    }
}
