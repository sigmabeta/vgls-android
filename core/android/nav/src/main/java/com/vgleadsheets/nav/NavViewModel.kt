package com.vgleadsheets.nav

import androidx.fragment.app.FragmentActivity
import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.PerfTracker
import com.vgleadsheets.repository.VglsRepository
import com.vgleadsheets.storage.Storage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NavViewModel @AssistedInject constructor(
    @Assisted initialState: NavState,
    @Assisted private val navigator: Navigator,
    private val repository: VglsRepository,
    private val storage: Storage,
    private val perfTracker: PerfTracker,
    private val dispatchers: VglsDispatchers,
    private val hatchet: Hatchet
) : MavericksViewModel<NavState>(initialState) {
    private var hudVisibilityTimer: Job? = null

    init {
        checkSavedPartSelection()
        checkLastUpdateTime()
        checkForUpdate()
        subscribeToPerfUpdates()
        showInitialScreen()
    }

    fun alwaysShowBack() = setState { copy(alwaysShowBack = true) }

    fun dontAlwaysShowBack() = setState { copy(alwaysShowBack = false) }

    fun setSelectedSong(song: Song) = setState {
        copy(
            selectedSong = song
        )
    }

    fun clearSelectedSong() = setState {
        copy(selectedSong = null)
    }

    fun hideHud() = withState { state ->
        stopHudVisibilityTimer()
        if (state.hudMode != HudMode.HIDDEN) {
            setState { copy(hudMode = HudMode.HIDDEN) }
        }
    }

    fun showHud() = withState { state ->
        stopHudVisibilityTimer()
        if (state.hudMode == HudMode.HIDDEN) {
            setState { copy(hudMode = HudMode.REGULAR) }
        }
    }

    fun onChangePartClick() = withState {
        if (it.hudMode != HudMode.PARTS) {
            showParts()
        }
    }

    fun onPartSelect(apiId: String) = setState {
        storage.saveSelectedPart(apiId)

        copy(
            selectedPart = Part.valueOf(apiId),
            hudMode = HudMode.REGULAR
        )
    }

    fun showSearch() = withState { state ->
        if (state.hudMode != HudMode.SEARCH) {
            setState { copy(hudMode = HudMode.SEARCH) }
        }
    }

    fun startHudVisibilityTimer() = withState { state ->
        stopHudVisibilityTimer()
        if (state.hudMode == HudMode.REGULAR) {
            hudVisibilityTimer = viewModelScope.launch(dispatchers.computation) {
                hatchet.v("Starting hud visibility timer.")
                delay(TIMEOUT_HUD_VISIBLE)

                hatchet.v("Hud visibility timer expired.")
                withContext(dispatchers.main) {
                    hideHud()
                }
                hudVisibilityTimer = null
            }
        }
    }

    fun stopHudTimer() {
        stopHudVisibilityTimer()
    }

    fun hideSearch() = withState { state ->
        if (state.hudMode == HudMode.SEARCH) {
            setState { copy(hudMode = HudMode.REGULAR) }
        }
    }

    fun bottomMenuButtonClick() = withState { state ->
        when (state.hudMode) {
            HudMode.PERF -> perfBottomMenuButtonClick(state.perfViewState.viewMode)
            HudMode.REGULAR -> toMenu()
            else -> toRegularMode()
        }
    }

    fun toMenu() = setState {
        copy(
            hudMode = HudMode.MENU
        )
    }

    fun showParts() = setState {
        copy(
            hudMode = HudMode.PARTS
        )
    }

    fun toRegularMode() = setState {
        copy(
            hudMode = HudMode.REGULAR,
            perfViewState = perfViewState.copy(
                viewMode = PerfViewMode.REGULAR
            )
        )
    }

    fun sheetDetailClick() = withState { state ->
        navigator.showSongDetail(state.selectedSong?.id!!)
    }

    fun youtubeSearchClick() = withState { state ->
        navigator.searchYoutube(state.selectedSong!!.name, state.selectedSong.gameName)
    }

    fun refresh() = withState {
        if (it.digest !is Loading) {
            repository.refresh()
                .execute {
                    copy(digest = it)
                }
        }
    }

    fun onRandomSelectClick(selectedPart: Part) = withState { _ ->
        viewModelScope.launch(dispatchers.disk) {
            val song = repository.getAllSongs()
                .map { it.filteredForVocals(selectedPart.apiId) }
                .filter { it.isNotEmpty() }
                .map { it.random() }
                .firstOrNull() ?: return@launch

            navigator.showSongViewer(song.id)
        }
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
                hudMode = HudMode.PERF,
                perfViewState = perfViewState.copy(
                    viewMode = PerfViewMode.REGULAR
                )
            )
        }
    }

    fun saveTopLevelScreen(screen: TopLevel) {
        storage.saveTopLevelScreen(screen.name)
    }

    fun favoritesClick() = withState { state ->
        state.selectedSong?.let {
            viewModelScope.launch(dispatchers.disk) {
                repository.toggleFavoriteSong(it.id)
            }
        }
    }

    fun alternateSheetClick() = withState { state ->
        state.selectedSong?.let {
            viewModelScope.launch(dispatchers.disk) {
                repository.toggleAlternate(it.id)
            }
        }
    }

    fun offlineClick() = withState { state ->
        state.selectedSong?.let {
            viewModelScope.launch(dispatchers.disk) {
                repository.toggleOfflineSong(it.id)
            }
        }
    }

    private fun showInitialScreen() {
        viewModelScope.launch(dispatchers.disk) {
            val selectionString = storage.getSavedTopLevelScreen()

            val selection = try {
                TopLevel.valueOf(selectionString)
            } catch (ex: IllegalArgumentException) {
                hatchet.e("Invalid default screen: $selectionString")
                TopLevel.GAME
            }

            withContext(dispatchers.main) {
                hatchet.v("Showing screen: $selection")
                showTopLevelScreen(selection)
            }
        }
    }

    private fun showTopLevelScreen(screen: TopLevel) {
        when (screen) {
            TopLevel.FAVORITE -> navigator.showFavorites()
            TopLevel.COMPOSER -> navigator.showComposerList()
            TopLevel.GAME -> navigator.showGameList()
            TopLevel.SONG -> navigator.showAllSheets()
            TopLevel.TAG -> navigator.showTagList()
        }
    }

    private fun checkSavedPartSelection() {
        viewModelScope.launch(dispatchers.disk) {
            val selection = storage.getSavedSelectedPart().ifEmpty { "C" }

            val part = try {
                Part.valueOf(selection)
            } catch (ex: IllegalArgumentException) {
                hatchet.e(

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

    private fun perfBottomMenuButtonClick(mode: PerfViewMode) {
        when (mode) {
            PerfViewMode.REGULAR -> toMenu()
            else -> toPerf()
        }
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

    private fun stopHudVisibilityTimer() {
        if (hudVisibilityTimer != null) {
            hatchet.v("Clearing hud visibility timer.")
            hudVisibilityTimer?.cancel()
            hudVisibilityTimer = null
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            initialState: NavState,
            navigator: Navigator
        ): NavViewModel
    }

    interface NavViewModelFactoryProvider {
        var navViewModelFactory: Factory
    }

    companion object : MavericksViewModelFactory<NavViewModel, NavState> {
        const val TIMEOUT_HUD_VISIBLE = 3000L

        override fun create(viewModelContext: ViewModelContext, state: NavState): NavViewModel {
            val activity =
                (viewModelContext as ActivityViewModelContext).activity<FragmentActivity>()
            val provider = activity as NavViewModelFactoryProvider
            return provider.navViewModelFactory.create(state, activity as Navigator)
        }
    }
}
