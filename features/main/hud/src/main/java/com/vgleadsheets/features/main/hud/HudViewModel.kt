package com.vgleadsheets.features.main.hud

import androidx.fragment.app.FragmentActivity
import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.storage.Storage
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import timber.log.Timber

class HudViewModel @AssistedInject constructor(
    @Assisted initialState: HudState,
    @Assisted private val router: FragmentRouter,
    private val repository: Repository,
    private val storage: Storage,
    private val perfTracker: PerfTracker
) : MvRxViewModel<HudState>(initialState) {
    private var timer: Disposable? = null

    private val searchOperations = CompositeDisposable()

    private val searchQueryQueue = BehaviorSubject.create<String>()

    init {
        checkSavedPartSelection()
        checkLastUpdateTime()
        checkForUpdate()
        subscribeToSearchQueryQueue()
        subscribeToPerfUpdates()
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
            .subscribe(
                {},
                {
                    // showError("Failed to save part selection: ${it.message}")
                }
            ).disposeOnClear()

        copy(
            selectedPart = Part.valueOf(apiId),
            mode = HudMode.REGULAR
        )
    }

    fun refresh() = withState {
        if (it.digest !is Loading) {
            repository.refresh()
                .execute { digest ->
                    copy(digest = digest)
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

    fun queueSearchQuery(query: String) {
        searchQueryQueue.onNext(query)
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
            timer = Observable.timer(TIMEOUT_HUD_VISIBLE, TimeUnit.MILLISECONDS)
                .subscribe {
                    hideHud()
                }.disposeOnClear()
        }
    }

    fun stopHudTimer() {
        stopTimer()
    }

    fun onRandomSelectClick(selectedPart: Part) = withState { _ ->
        repository
            .getAllSongs()
            .firstOrError()
            .map { songs ->
                songs.filteredForVocals(selectedPart.apiId)
            }
            .map { it.random() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { song ->
                    if (song == null) {
                        // showError("Failed to get a random track.")
                        return@subscribe
                    }

                    // TODO In one-shot stream
                    /*val transposition = state.selectedPart.apiId

                    tracker.logRandomSongView(
                        song.name,
                        song.gameName,
                        transposition
                    )*/

                    router.showSongViewer(song.id)
                },
                {
                    // showError("Failed to get a random track.")
                }
            ).disposeOnClear()
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

    private fun showInitialScreen() {
        storage.getSavedTopLevelScreen()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { savedId ->
                    val selection = savedId.ifEmpty { HudFragment.TOP_LEVEL_SCREEN_ID_DEFAULT }

                    Timber.v("Showing screen: $selection")
                    showScreen(selection)
                },
                {
                    Timber.w("No screen ID found, going with default.")
                    showScreen(HudFragment.TOP_LEVEL_SCREEN_ID_DEFAULT)
                }
            ).disposeOnClear()
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

    private fun checkSavedPartSelection() = withState {
        storage.getSavedSelectedPart()
            .subscribe(
                { partId ->
                    val selection = partId.ifEmpty { "C" }

                    val selectedPart = try {
                        Part.valueOf(selection)
                    } catch (ex: IllegalArgumentException) {
                        Timber.e("${ex.message}: value $selection no longer valid; defaulting to C")
                        Part.C
                    }

                    setState {
                        copy(
                            selectedPart = selectedPart,
                        )
                    }
                    showInitialScreen()
                },
                {
                    Timber.w("No part selection found, going with default.")
                    showInitialScreen()
                }
            ).disposeOnClear()
    }

    private fun checkLastUpdateTime() = repository.getLastUpdateTime()
        .map { it.timeMs }
        .execute { newTime ->
            copy(updateTime = newTime)
        }

    private fun checkForUpdate() = repository.checkShouldAutoUpdate()
        .toObservable()
        .subscribe(
            { shouldRefresh ->
                if (shouldRefresh) {
                    refresh()
                }
            },
            {
                Timber.e("Failed to check update time: ${it.message}")
            }
        )

    private fun startSearchQuery(searchQuery: String) {
        withState { state ->
            if (state.searchQuery != searchQuery) {
                searchOperations.clear()

                setState {
                    copy(
                        searchQuery = searchQuery,
                        searchResults = searchResults.copy(
                            searching = true
                        )
                    )
                }

                val gameSearch = repository.searchGamesCombined(searchQuery)
                    .debounce(THRESHOLD_RESULT_DEBOUNCE, TimeUnit.MILLISECONDS)
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

                val songSearch = repository.searchSongs(searchQuery)
                    .debounce(THRESHOLD_RESULT_DEBOUNCE, TimeUnit.MILLISECONDS)
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

                val composerSearch = repository.searchComposersCombined(searchQuery)
                    .debounce(THRESHOLD_RESULT_DEBOUNCE, TimeUnit.MILLISECONDS)
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

                searchOperations.addAll(gameSearch, songSearch, composerSearch)
            }
        }
    }

    private fun perfBottomMenuButtonClick(mode: PerfViewMode) {
        when (mode) {
            PerfViewMode.REGULAR -> toMenu()
            else -> toPerf()
        }
    }

    private fun subscribeToSearchQueryQueue() {
        searchQueryQueue
            .throttleLast(THRESHOLD_SEARCH_INPUTS, TimeUnit.MILLISECONDS)
            .subscribe(
                { startSearchQuery(it) },
                { }
            ).disposeOnClear()
    }

    private fun subscribeToPerfUpdates() {
        perfTracker.screenLoadStream()
            .subscribe {
                setState {
                    copy(loadTimeLists = it)
                }
            }
            .disposeOnClear()

        perfTracker.frameTimeStream()
            .subscribe {
                setState {
                    copy(frameTimeStatsMap = it)
                }
            }
            .disposeOnClear()

        perfTracker.invalidateStream()
            .subscribe {
                setState {
                    copy(invalidateStatsMap = it)
                }
            }
            .disposeOnClear()
    }

    private fun stopTimer() {
        timer?.dispose()
    }

    fun sheetDetailClick() = withState { state ->
        router.showSheetDetail(state.selectedSong?.id!!)
    }

    fun youtubeSearchClick() = withState { state ->
        router.searchYoutube(state.selectedSong!!.name, state.selectedSong.gameName)
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: HudState,
            router: FragmentRouter
        ): HudViewModel
    }

    interface HudViewModelFactoryProvider {
        var hudViewModelFactory: Factory
    }

    companion object : MvRxViewModelFactory<HudViewModel, HudState> {
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
