package com.vgleadsheets.features.main.hud

import androidx.fragment.app.FragmentActivity
import com.airbnb.mvrx.ActivityViewModelContext
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.model.filteredForVocals
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.storage.Storage
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import timber.log.Timber

@Suppress("TooManyFunctions")
class HudViewModel @AssistedInject constructor(
    @Assisted initialState: HudState,
    private val repository: Repository,
    private val storage: Storage,
    private val perfTracker: PerfTracker
) : MvRxViewModel<HudState>(initialState) {
    private var timer: Disposable? = null

    init {
        checkSavedPartSelection()
        checkLastUpdateTime()
        checkForUpdate()
        subscribeToPerfUpdates()
    }

    fun alwaysShowBack() = setState { copy(alwaysShowBack = true) }

    fun dontAlwaysShowBack() = setState { copy(alwaysShowBack = false) }

    fun onMenuClick() = withState {
        when (it.mode) {
            HudMode.PERF -> when (it.perfViewState.viewMode) {
                PerfViewMode.REGULAR -> backToMenu()
                else -> backoPerfRegular()
            }
            HudMode.REGULAR -> showMenu()
            else -> hideMenus()
        }
    }

    fun onChangePartClick() = withState {
        if (it.mode != HudMode.PARTS) {
            showParts()
        }
    }

    fun onMenuAction() {
        hideMenus()
    }

    fun setSelectedSong(song: Song) = setState {
        copy(
            selectedSong = song
        )
    }

    fun clearSelectedSong() = setState {
        copy(selectedSong = null)
    }

    fun onPartSelect(apiId: String) = setState {
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

    fun searchClick() = withState { state ->
        if (state.mode != HudMode.SEARCH) {
            setState { copy(mode = HudMode.SEARCH) }
        }
    }

    fun searchQuery(query: String?) = withState { state ->
        if (state.mode == HudMode.SEARCH) {
            setState { copy(searchQuery = query) }
        }
    }

    fun hideSearch() = withState { state ->
        if (state.mode == HudMode.SEARCH) {
            setState { copy(mode = HudMode.REGULAR) }
        }
    }

    fun exitSearch() = withState { state ->
        if (state.mode == HudMode.SEARCH) {
            setState { copy(mode = HudMode.REGULAR, searchQuery = null) }
        }
    }

    fun hideHud() = withState { state ->
        if (state.hudVisible) {
            setState { copy(hudVisible = false) }
        }
    }

    fun showHud() = withState { state ->
        stopTimer()
        if (!state.hudVisible) {
            setState { copy(hudVisible = true) }
        }
    }

    fun startHudTimer() = withState { state ->
        stopTimer()
        if (state.mode == HudMode.REGULAR) {
            timer = Observable.timer(TIMEOUT_HUD_VISIBLE, TimeUnit.MILLISECONDS)
                .execute { timer ->
                    if (timer is Success) {
                        copy(hudVisible = false)
                    } else {
                        this
                    }
                }
        }
    }

    fun stopHudTimer() {
        stopTimer()
    }

    fun onRandomSelectClick(selectedPart: Part) {
        repository
            .getAllSongs()
            .firstOrError()
            .map { songs ->
                songs.filteredForVocals(selectedPart.apiId)
            }
            .map { it.random() }
            .execute {
                copy(random = it)
            }
    }

    fun clearRandom() = setState {
        copy(random = Uninitialized)
    }

    fun clearDigestError() = setState {
        copy(digest = Uninitialized)
    }

    private fun hideMenus() = setState {
        copy(
            mode = HudMode.REGULAR,
            perfViewState = perfViewState.copy(viewMode = PerfViewMode.REGULAR)
        )
    }

    private fun showMenu() = setState {
        copy(
            mode = HudMode.MENU
        )
    }

    private fun showParts() = setState {
        copy(
            mode = HudMode.PARTS
        )
    }

    private fun backoPerfRegular() = setState {
        copy(
            perfViewState = perfViewState.copy(viewMode = PerfViewMode.REGULAR)
        )
    }

    private fun backToMenu() = setState {
        copy(
            mode = HudMode.MENU
        )
    }

    private fun checkSavedPartSelection() = withState {
        storage.getSavedSelectedPart().subscribe(
            {
                val selection = it.ifEmpty {
                    "C"
                }

                setState {
                    copy(
                        selectedPart = Part.valueOf(selection),
                        readyToShowScreens = true
                    )
                }
            },
            {
                Timber.w("No part selection found, going with default.")
                setState {
                    copy(
                        readyToShowScreens = true
                    )
                }
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

    fun onPerfClick() {
        perfTracker.requestUpdates()
        setState {
            copy(mode = HudMode.PERF)
        }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: HudState): HudViewModel
    }

    interface HudViewModelFactoryProvider {
        var hudViewModelFactory: Factory
    }

    companion object : MvRxViewModelFactory<HudViewModel, HudState> {
        const val TIMEOUT_HUD_VISIBLE = 3000L

        override fun create(viewModelContext: ViewModelContext, state: HudState): HudViewModel {
            val activity =
                (viewModelContext as ActivityViewModelContext).activity<FragmentActivity>()
            val provider = activity as HudViewModelFactoryProvider
            return provider.hudViewModelFactory.create(state)
        }
    }
}
