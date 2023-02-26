package com.vgleadsheets.features.main.hud

import androidx.fragment.app.FragmentActivity
import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Jam
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.storage.Storage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.UnknownHostException

@OptIn(FlowPreview::class)
class HudViewModel @AssistedInject constructor(
    @Assisted initialState: HudState,
    @Assisted private val router: FragmentRouter,
    private val repository: com.vgleadsheets.repository.VglsRepository,
    private val storage: Storage,
    private val perfTracker: PerfTracker,
    private val dispatchers: VglsDispatchers,
    private val hatchet: Hatchet
) : MavericksViewModel<HudState>(initialState) {
    private var hudVisibilityTimer: Job? = null

    // Card shark
    private var jamJob = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    private var gameSearch: Job? = null
    private var composerSearch: Job? = null
    private var songSearch: Job? = null

    private val searchQueryQueue = MutableSharedFlow<String>()

    init {
        checkSavedPartSelection()
        checkLastUpdateTime()
        checkForUpdate()
        subscribeToSearchQueryQueue()
        subscribeToPerfUpdates()
        showInitialScreen()
    }

    fun followJam(jamId: Long) {
        unfollowJam()

        // I'm pretty sure what this means is that cancelling jamJob will cancel this job.
        viewModelScope.launch(dispatchers.disk + jamJob) {
            hatchet.i(this.javaClass.simpleName, "Following jam.")
            repository.getJam(jamId, false)
                .catch {
                    val message = "Failed to get Jam from database: ${it.message}"
                    hatchet.e(this.javaClass.simpleName, message)
                }
                .onEach { subscribeToJamNetwork(it) }
                .firstOrNull()
        }

        subscribeToJamDatabase(jamId)
    }

    fun unfollowJam() {
        if (jamJob.isCancelled) {
            return
        }

        jamJob.cancel()
        setState { copy(activeJam = null) }
    }

    fun alwaysShowBack() = setState { copy(alwaysShowBack = true) }

    fun dontAlwaysShowBack() = setState { copy(alwaysShowBack = false) }

    fun onChangePartClick() = withState {
        if (it.mode != HudMode.PARTS) {
            showParts()
        }
    }

    fun setSelectedSong(song: Song) = setState {
        copy(
            selectedSong = song
        )
    }

    fun clearSelectedSong() = setState {
        copy(selectedSong = null)
    }

    fun setViewerScreenVisible() = setState {
        copy(viewerScreenVisible = true)
    }

    fun setViewerScreenNotVisible() = setState {
        copy(viewerScreenVisible = false)
    }

    fun onPartSelect(apiId: String) = setState {
        storage.saveSelectedPart(apiId)

        copy(
            selectedPart = Part.valueOf(apiId),
            mode = HudMode.REGULAR
        )
    }

    fun refresh() = withState {
        if (it.digest !is Loading) {
            repository.refresh()
                .execute {
                    copy(digest = it)
                }
        }
    }

    fun showSearch() = withState { state ->
        if (state.mode != HudMode.SEARCH) {
            setState { copy(mode = HudMode.SEARCH) }
        }
    }

    fun clearSearchQuery() = setState {
        copy(
            searchQuery = null,
            searchResults = searchResults.copy(
                songs = Uninitialized,
                composers = Uninitialized,
                games = Uninitialized
            )
        )
    }

    fun queueSearchQuery(query: String) = viewModelScope.launch(dispatchers.computation) {
        searchQueryQueue.emit(query)
    }

    fun hideSearch() = withState { state ->
        if (state.mode == HudMode.SEARCH) {
            setState { copy(mode = HudMode.REGULAR) }
        }
    }

    fun hideHud() = withState { state ->
        if (state.mode != HudMode.HIDDEN) {
            setState { copy(mode = HudMode.HIDDEN) }
        }
    }

    fun showHud() = withState { state ->
        stopTimer()
        if (state.mode == HudMode.HIDDEN) {
            setState { copy(mode = HudMode.REGULAR) }
        }
    }

    fun startHudTimer() = withState { state ->
        stopTimer()
        if (state.mode == HudMode.REGULAR) {
            hudVisibilityTimer = viewModelScope.launch(dispatchers.computation) {
                delay(TIMEOUT_HUD_VISIBLE)

                withContext(dispatchers.main) {
                    hideHud()
                }
            }
        }
    }

    fun stopHudTimer() {
        stopTimer()
    }

    fun onRandomSelectClick(selectedPart: Part) = withState { _ ->
        viewModelScope.launch(dispatchers.disk) {
            val song = repository.getAllSongs()
                .map { it.filteredForVocals(selectedPart.apiId) }
                .filter { it.isNotEmpty() }
                .map { it.random() }
                .firstOrNull() ?: return@launch

            router.showSongViewer(song.id)
        }
    }

    fun bottomMenuButtonClick() = withState { state ->
        when (state.mode) {
            HudMode.PERF -> perfBottomMenuButtonClick(state.perfViewState.viewMode)
            HudMode.REGULAR -> toMenu()
            else -> toRegularMode()
        }
    }

    fun toMenu() = setState {
        copy(
            mode = HudMode.MENU
        )
    }

    fun showParts() = setState {
        copy(
            mode = HudMode.PARTS
        )
    }

    fun toRegularMode() = setState {
        copy(
            mode = HudMode.REGULAR,
            searchQuery = null,
            perfViewState = perfViewState.copy(
                viewMode = PerfViewMode.REGULAR
            )
        )
    }

    fun setPerfSelectedScreen(screen: PerfSpec) {
        setState {
            perfTracker.requestUpdates()

            val newPerfViewState = perfViewState.copy(
                selectedScreen = screen
            )
            copy(
                perfViewState = newPerfViewState
            )
        }
    }

    fun setPerfViewMode(perfViewMode: PerfViewMode) {
        setState {
            perfTracker.requestUpdates()

            val newPerfViewState = perfViewState.copy(
                viewMode = perfViewMode
            )
            copy(
                perfViewState = newPerfViewState
            )
        }
    }

    fun toPerf() {
        perfTracker.requestUpdates()
        setState {
            copy(
                mode = HudMode.PERF,
                perfViewState = perfViewState.copy(
                    viewMode = PerfViewMode.REGULAR
                )
            )
        }
    }

    fun saveTopLevelScreen(screenId: String) {
        val shouldSave = screenId != HudFragment.MODAL_SCREEN_ID_DEBUG &&
            screenId != HudFragment.MODAL_SCREEN_ID_SETTINGS

        if (shouldSave) {
            storage.saveTopLevelScreen(screenId)
        }
    }

    fun sheetDetailClick() = withState { state ->
        router.showSheetDetail(state.selectedSong?.id!!)
    }

    fun youtubeSearchClick() = withState { state ->
        router.searchYoutube(state.selectedSong!!.name, state.selectedSong.gameName)
    }

    private fun showInitialScreen() {
        viewModelScope.launch(dispatchers.disk) {
            val selection = storage.getSavedTopLevelScreen()
                .ifEmpty {
                    HudFragment.TOP_LEVEL_SCREEN_ID_DEFAULT
                }

            withContext(dispatchers.main) {
                hatchet.v(this.javaClass.simpleName, "Showing screen: $selection")
                showScreen(selection)
            }
        }
    }

    private fun showScreen(topLevelScreenIdDefault: String) {
        when (topLevelScreenIdDefault) {
            HudFragment.TOP_LEVEL_SCREEN_ID_COMPOSER -> router.showComposerList()
            HudFragment.TOP_LEVEL_SCREEN_ID_GAME -> router.showGameList()
            HudFragment.TOP_LEVEL_SCREEN_ID_JAM -> router.showJams()
            HudFragment.TOP_LEVEL_SCREEN_ID_SONG -> router.showAllSheets()
            HudFragment.TOP_LEVEL_SCREEN_ID_TAG -> router.showTagList()
            else -> router.showGameList()
        }
    }

    private fun checkSavedPartSelection() {
        viewModelScope.launch(dispatchers.disk) {
            val selection = storage.getSavedSelectedPart().ifEmpty { "C" }

            val part = try {
                Part.valueOf(selection)
            } catch (ex: IllegalArgumentException) {
                hatchet.e(
                    this.javaClass.simpleName,
                    "${ex.message}: value $selection no longer valid; defaulting to C"
                )
                Part.C
            }
            setState {
                copy(selectedPart = part)
            }
        }
    }

    private fun checkLastUpdateTime() = repository.getLastUpdateTime()
        .map { it.timeMs }
        .execute { newTime ->
            copy(updateTime = newTime)
        }

    private fun checkForUpdate() {
        viewModelScope.launch(dispatchers.disk) {
            val shouldRefresh = repository.checkShouldAutoUpdate()

            if (shouldRefresh) {
                refresh()
            }
        }
    }

    private fun startSearchQuery(searchQuery: String) {
        withState { state ->
            if (state.searchQuery != searchQuery) {
                cancelSearch()

                setState {
                    copy(
                        searchQuery = searchQuery,
                        searchResults = searchResults.copy(
                            searching = true
                        )
                    )
                }

                gameSearch = repository.searchGamesCombined(searchQuery)
                    .debounce(THRESHOLD_RESULT_DEBOUNCE)
                    .execute { newGames ->
                        if (newGames is Loading) {
                            return@execute this
                        }

                        copy(
                            searchResults = searchResults.copy(
                                games = newGames,
                                searching = false
                            )
                        )
                    }

                songSearch = repository.searchSongsCombined(searchQuery)
                    .debounce(THRESHOLD_RESULT_DEBOUNCE)
                    .execute { newSongs ->
                        if (newSongs is Loading) {
                            return@execute this
                        }

                        copy(
                            searchResults = searchResults.copy(
                                songs = newSongs,
                                searching = false
                            )
                        )
                    }

                composerSearch = repository.searchComposersCombined(searchQuery)
                    .debounce(THRESHOLD_RESULT_DEBOUNCE)
                    .execute { newComposers ->
                        if (newComposers is Loading) {
                            return@execute this
                        }

                        copy(
                            searchResults = searchResults.copy(
                                composers = newComposers,
                                searching = false
                            )
                        )
                    }
            }
        }
    }

    private fun cancelSearch() {
        gameSearch?.cancel()
        composerSearch?.cancel()
        songSearch?.cancel()
    }

    private fun perfBottomMenuButtonClick(mode: PerfViewMode) {
        when (mode) {
            PerfViewMode.REGULAR -> toMenu()
            else -> toPerf()
        }
    }

    private fun subscribeToSearchQueryQueue() {
        searchQueryQueue
            .sample(THRESHOLD_SEARCH_INPUTS)
            .onEach { startSearchQuery(it) }
            .flowOn(dispatchers.computation)
            .launchIn(viewModelScope)
    }

    private fun subscribeToPerfUpdates() {
        perfTracker.screenLoadStream()
            .onEach {
                setState {
                    copy(loadTimeLists = it)
                }
            }
            .flowOn(dispatchers.computation)
            .launchIn(viewModelScope)

        perfTracker.frameTimeStream()
            .onEach {
                setState {
                    copy(frameTimeStatsMap = it)
                }
            }
            .flowOn(dispatchers.computation)
            .launchIn(viewModelScope)

        perfTracker.invalidateStream()
            .onEach {
                setState {
                    copy(invalidateStatsMap = it)
                }
            }
            .flowOn(dispatchers.computation)
            .launchIn(viewModelScope)
    }

    private fun stopTimer() {
        hudVisibilityTimer?.cancel()
    }

    // Jam management

    private fun subscribeToJamDatabase(jamId: Long) {
        hatchet.i(this.javaClass.simpleName, "Subscribing to jam $jamId in the database.")
        repository.observeJamState(jamId)
            .onEach {
                setState {
                    // TODO Do more than a null check here; should we report an error if the *song* is null?
                    copy(activeJam = it)
                }
            }.catch {
                val message = "Error observing Jam: ${it.message}"
                hatchet.e(this.javaClass.simpleName, message)
            }
            .flowOn(dispatchers.computation)
            .launchIn(viewModelScope + jamJob)
    }

    private fun subscribeToJamNetwork(jam: Jam) {
        hatchet.i(this.javaClass.simpleName, "Subscribing to jam ${jam.id} on the network.")
        repository.refreshJamStateContinuously(jam.name)
            .onEach { }
            .catch {
                val message: String
                if (it is HttpException) {
                    if (it.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                        message = "Jam has been deleted from server."
                        removeJam(jam.id)
                    } else {
                        message = "Error communicating with Jam server."
                    }
                } else if (it is UnknownHostException) {
                    message = "Can't reach Jam server. Check connection and try again."
                } else {
                    message = "Error communicating with Jam server."
                }

                hatchet.e(this.javaClass.simpleName, message)
            }
            .flowOn(dispatchers.computation)
            .launchIn(viewModelScope + jamJob)
    }

    @Suppress("TooGenericExceptionCaught")
    private fun removeJam(dataId: Long) {
        viewModelScope.launch(dispatchers.disk) {
            try {
                repository.removeJam(dataId)
            } catch (ex: Exception) {
                hatchet.e(this.javaClass.simpleName, "Error removing Jam: ${ex.message}")
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: HudState,
            router: FragmentRouter
        ): HudViewModel
    }

    interface HudViewModelFactoryProvider {
        var hudViewModelFactory: Factory
    }

    companion object : MavericksViewModelFactory<HudViewModel, HudState> {
        const val TIMEOUT_HUD_VISIBLE = 3000L

        const val THRESHOLD_SEARCH_INPUTS = 500L
        const val THRESHOLD_RESULT_DEBOUNCE = 250L

        override fun create(viewModelContext: ViewModelContext, state: HudState): HudViewModel {
            val activity =
                (viewModelContext as ActivityViewModelContext).activity<FragmentActivity>()
            val provider = activity as HudViewModelFactoryProvider
            return provider.hudViewModelFactory.create(state, activity as FragmentRouter)
        }
    }
}
