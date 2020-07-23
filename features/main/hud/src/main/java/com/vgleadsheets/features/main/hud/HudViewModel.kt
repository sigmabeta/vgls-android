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
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.perf.view.common.PerfView
import com.vgleadsheets.perf.view.common.PerfViewScreenStatus
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.storage.Storage
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit

@Suppress("TooManyFunctions")
class HudViewModel @AssistedInject constructor(
    @Assisted initialState: HudState,
    private val repository: Repository,
    private val storage: Storage
) : MvRxViewModel<HudState>(initialState),
    PerfView {
    private var timer: Disposable? = null

    private val perfViewTimers: MutableMap<String, Disposable> = HashMap()

    init {
        checkSavedPartSelection()
        checkLastUpdateTime()
        checkForUpdate()
    }

    fun alwaysShowBack() = setState { copy(alwaysShowBack = true) }

    fun dontAlwaysShowBack() = setState { copy(alwaysShowBack = false) }

    fun onMenuClick() = withState {
        if (it.menuExpanded) {
            hideMenu()
        } else {
            showMenu()
        }
    }

    fun onMenuBackPress() {
        hideMenu()
    }

    fun onMenuAction() {
        setState { copy(menuExpanded = false) }
    }

    fun setAvailableParts(parts: List<Part>) = withState { state ->
        if (state.parts == parts) {
            return@withState
        }

        Timber.i("Setting available parts: $parts")

        val selectedId = state.parts.first { part -> part.selected }.apiId
        setState {
            copy(parts = PartSelectorItem.getAvailablePartPickerItems(parts, selectedId))
        }
    }

    fun resetAvailableParts() = withState {
        val selectedId = it.parts.first { part -> part.selected }.apiId
        setState {
            copy(parts = PartSelectorItem.getDefaultPartPickerItems(selectedId))
        }
    }

    fun onPartSelect(apiId: String) = withState { state ->
        setState {
            copy(parts = setSelection(apiId, state.parts), menuExpanded = false)
        }
    }

    fun refresh() = withState {
        if (it.digest !is Loading) {
            repository.forceRefresh()
                .execute { digest ->
                    copy(digest = digest)
                }
        }
    }

    fun searchClick() = withState { state ->
        if (!state.searchVisible) {
            setState { copy(searchVisible = true) }
        }
    }

    fun searchQuery(query: String?) = withState { state ->
        if (state.searchVisible) {
            setState { copy(searchQuery = query) }
        }
    }

    fun exitSearch() = withState { state ->
        if (state.searchVisible) {
            setState { copy(searchVisible = false, searchQuery = null) }
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
        if (!state.menuExpanded) {
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

    fun onRandomSelectClick(selectedPart: PartSelectorItem) {
        repository
            .getAllSongs()
            .firstOrError()
            .map { songs ->
                songs.filter { song ->
                    song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
                }
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

    fun clearPerfTimers() {
        perfViewTimers.keys.forEach { screenName ->
            removePerfScreenStatus(screenName)
        }
    }

    override fun start(screenName: String) = withState {
        // This must be created outside setState to make it a pure reducer..
        val newScreenStatus = PerfViewScreenStatus(screenName)

        setState {
            Timber.d("Adding perf status view for screen $screenName...")

            val newScreenStatuses = perfViewStatus.screenStatuses.filter {
                it.screenName != screenName
            } + newScreenStatus

            copy(
                perfViewStatus = perfViewStatus.copy(
                    screenStatuses = newScreenStatuses
                )
            )
        }
    }

    override fun completed(screenName: String) = updatePerfStatus(screenName, completed = true)

    override fun cancelled(screenName: String) = updatePerfStatus(screenName, cancelled = true)

    override fun viewCreated(screenName: String) = updatePerfStatus(screenName, viewCreated = true)

    override fun titleLoaded(screenName: String) = updatePerfStatus(screenName, titleLoaded = true)

    override fun transitionStarted(screenName: String) =
        updatePerfStatus(screenName, transitionStarted = true)

    override fun partialContentLoaded(screenName: String) =
        updatePerfStatus(screenName, partialContentLoaded = true)

    override fun fullContentLoaded(screenName: String) =
        updatePerfStatus(screenName, fullContentLoaded = true)

    private fun removePerfScreenStatus(screenName: String) = setState {
        Timber.d("Removing perf status view for screen $screenName...")
        val timer = perfViewTimers[screenName]

        if (timer?.isDisposed == false) {
            timer.dispose()
        }

        perfViewTimers.remove(screenName)

        val newScreenStatuses = perfViewStatus.screenStatuses.filter {
            it.screenName != screenName
        }

        copy(
            perfViewStatus = perfViewStatus.copy(
                screenStatuses = newScreenStatuses
            )
        )
    }

    private fun hideMenu() = setState { copy(menuExpanded = false) }

    private fun showMenu() = setState { copy(menuExpanded = true) }

    private fun checkSavedPartSelection() = withState {
        storage.getSavedSelectedPart().subscribe(
            {
                val selection = if (it.isEmpty()) {
                    "C"
                } else {
                    it
                }

                setState {
                    copy(
                        parts = PartSelectorItem.getDefaultPartPickerItems(selection),
                        readyToShowScreens = true
                    )
                }
            },
            {
                Timber.w("No part selection found, going with default.")
                setState {
                    copy(
                        parts = PartSelectorItem.getDefaultPartPickerItems("C"),
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

    private fun checkForUpdate() = repository.checkForUpdate()
        .toObservable()
        .execute {
            copy(digest = it)
        }

    private fun stopTimer() {
        timer?.dispose()
    }

    private fun setSelection(selection: String, oldList: List<PartSelectorItem>) =
        oldList.map { item ->
            item.copy(selected = selection == item.apiId)
        }

    private fun updatePerfStatus(
        screenName: String,
        completed: Boolean? = null,
        cancelled: Boolean? = null,
        viewCreated: Boolean? = null,
        titleLoaded: Boolean? = null,
        transitionStarted: Boolean? = null,
        partialContentLoaded: Boolean? = null,
        fullContentLoaded: Boolean? = null
    ) = withState {
        // These must be retrieved outside setState to make it a pure reducer..
        val oldScreenStatus = it.perfViewStatus.getScreenByName(screenName)

        if (oldScreenStatus == null) {
            Timber.w("No PerfViewScreenStatus found for screen $screenName.")
            return@withState
        }

        val duration = System.currentTimeMillis() - oldScreenStatus.startTime

        setState {
            if (completed == true) {
                startPerfCompleteTimer(screenName)
            }

            if (cancelled == true) {
                startPerfCancelledTimer(screenName)
            }

            val newScreenStatus = oldScreenStatus.copy(
                completed = if (completed == true) true else oldScreenStatus.completed,
                cancellationDuration = if (cancelled == true) duration else oldScreenStatus.cancellationDuration,
                viewCreationDuration = if (viewCreated == true) duration else oldScreenStatus.viewCreationDuration,
                titleLoadDuration = if (titleLoaded == true) duration else oldScreenStatus.titleLoadDuration,
                transitionStartDuration = if (transitionStarted == true) duration else oldScreenStatus.transitionStartDuration,
                partialContentLoadDuration = if (partialContentLoaded == true) duration else oldScreenStatus.partialContentLoadDuration,
                fullContentLoadDuration = if (fullContentLoaded == true) duration else oldScreenStatus.fullContentLoadDuration
            )

            val newStatuses = this.perfViewStatus.screenStatuses.replace(newScreenStatus) {
                it.screenName == screenName
            }

            copy(
                perfViewStatus = this.perfViewStatus.copy(
                    screenStatuses = newStatuses
                )
            )
        }
    }

    private fun startPerfCompleteTimer(screenName: String) {
        startPerfTimer(screenName, TIMEOUT_PERF_COMPLETE_CLEAR)
    }

    private fun startPerfCancelledTimer(screenName: String) {
        startPerfTimer(screenName, TIMEOUT_PERF_CANCEL_CLEAR)
    }

    private fun startPerfTimer(screenName: String, timeout: Long) {
        val timer = Observable.timer(timeout, TimeUnit.MILLISECONDS, Schedulers.io())
            .subscribe {
                removePerfScreenStatus(screenName)
            }

        perfViewTimers[screenName] = timer
    }

    private fun <T> List<T>.replace(newValue: T, block: (T) -> Boolean) = map {
        if (block(it)) newValue else it
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

        const val TIMEOUT_PERF_COMPLETE_CLEAR = 3000L
        const val TIMEOUT_PERF_CANCEL_CLEAR = 1000L

        override fun create(viewModelContext: ViewModelContext, state: HudState): HudViewModel? {
            val activity =
                (viewModelContext as ActivityViewModelContext).activity<FragmentActivity>()
            val provider = activity as HudViewModelFactoryProvider
            return provider.hudViewModelFactory.create(state)
        }
    }
}
