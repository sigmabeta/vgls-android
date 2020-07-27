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
import com.vgleadsheets.perf.tracking.common.PerfStage
import com.vgleadsheets.perf.tracking.common.PerfTracker
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
    private val storage: Storage,
    private val perfTracker: PerfTracker
) : MvRxViewModel<HudState>(initialState) {
    private var timer: Disposable? = null

    private val perfViewTimers: MutableMap<String, Disposable> = HashMap()

    init {
        checkSavedPartSelection()
        checkLastUpdateTime()
        checkForUpdate()
        subscribeToPerfUpdates()
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
        // This copy step prevents concurrentmod exceptions
        val timerKeys = perfViewTimers
            .keys
            .toMutableList()

        timerKeys.forEach { screenName ->
            removePerfScreenStatus(screenName)
        }
    }

    private fun start(
        screenName: String
    ) = withState {
        // This must be created outside setState to make it a pure reducer..
        val newScreenStatus = PerfViewScreenStatus(
            screenName,
            hashMapOf(
                Pair(PerfStage.VIEW_CREATED.toString(), 50L),
                Pair(PerfStage.TITLE_LOADED.toString(), 600L),
                Pair(PerfStage.TRANSITION_START.toString(), 800L),
                Pair(PerfStage.PARTIAL_CONTENT_LOAD.toString(), 4000L),
                Pair(PerfStage.FULL_CONTENT_LOAD.toString(), 4000L),
                Pair("completion", 5000L)
            )
        )

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

    private fun completed(screenName: String, duration: Long) =
        updatePerfStatus(screenName, duration, completed = true)

    private fun cancelled(screenName: String, duration: Long) =
        updatePerfStatus(screenName, duration, cancelled = true)

    private fun viewCreated(screenName: String, duration: Long) =
        updatePerfStatus(screenName, duration, viewCreated = true)

    private fun titleLoaded(screenName: String, duration: Long) =
        updatePerfStatus(screenName, duration, titleLoaded = true)

    private fun transitionStarted(screenName: String, duration: Long) =
        updatePerfStatus(screenName, duration, transitionStarted = true)

    private fun partialContentLoaded(screenName: String, duration: Long) =
        updatePerfStatus(screenName, duration, partialContentLoaded = true)

    private fun fullContentLoaded(screenName: String, duration: Long) =
        updatePerfStatus(screenName, duration, fullContentLoaded = true)

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

    private fun subscribeToPerfUpdates() {
        perfTracker.getEventStream()
            .subscribe {
                when (it.perfStage) {
                    PerfStage.VIEW_CREATED -> viewCreated(it.screenName, it.duration)
                    PerfStage.TITLE_LOADED -> titleLoaded(it.screenName, it.duration)
                    PerfStage.TRANSITION_START -> transitionStarted(it.screenName, it.duration)
                    PerfStage.PARTIAL_CONTENT_LOAD -> partialContentLoaded(
                        it.screenName,
                        it.duration
                    )
                    PerfStage.FULL_CONTENT_LOAD -> fullContentLoaded(it.screenName, it.duration)
                    PerfStage.CANCELLATION -> cancelled(it.screenName, it.duration)
                    PerfStage.COMPLETION -> completed(it.screenName, it.duration)
                    null -> start(it.screenName)
                }
            }
            .disposeOnClear()
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
        duration: Long,
        completed: Boolean? = null,
        cancelled: Boolean? = null,
        viewCreated: Boolean? = null,
        titleLoaded: Boolean? = null,
        transitionStarted: Boolean? = null,
        partialContentLoaded: Boolean? = null,
        fullContentLoaded: Boolean? = null
    ) = withState {
        // These must be retrieved outside setState to make it a pure reducer..
        val oldStatus = it.perfViewStatus.getScreenByName(screenName)

        if (oldStatus == null) {
            Timber.w("No PerfViewScreenStatus found for screen $screenName.")
            return@withState
        }

        setState {
            if (completed == true) {
                startPerfCompleteTimer(screenName)
            }

            if (cancelled == true) {
                startPerfCancelledTimer(screenName)
            }

            val newDurations = getUpdatedHashmap(
                oldStatus,
                viewCreated,
                duration,
                titleLoaded,
                transitionStarted,
                partialContentLoaded,
                fullContentLoaded
            )

            val newScreenStatus = oldStatus.copy(
                completionDuration = if (completed == true) duration else oldStatus.completionDuration,
                cancellationDuration = if (cancelled == true) duration else oldStatus.cancellationDuration,
                durations = newDurations
            )

            val newStatuses = this.perfViewStatus
                .screenStatuses
                .replace(newScreenStatus) { status ->
                    status.screenName == screenName
                }

            copy(
                perfViewStatus = this.perfViewStatus.copy(
                    screenStatuses = newStatuses
                )
            )
        }
    }

    private fun getUpdatedHashmap(
        oldStatus: PerfViewScreenStatus,
        viewCreated: Boolean?,
        duration: Long,
        titleLoaded: Boolean?,
        transitionStarted: Boolean?,
        partialContentLoaded: Boolean?,
        fullContentLoaded: Boolean?
    ): HashMap<String, Long> {
        val newDurations = hashMapOf<String, Long>()

        val oldViewCreated = oldStatus.durations[PerfStage.VIEW_CREATED.toString()]
        val oldTitleLoaded = oldStatus.durations[PerfStage.TITLE_LOADED.toString()]
        val oldTransitionStarted = oldStatus.durations[PerfStage.TRANSITION_START.toString()]
        val oldPartialContent = oldStatus.durations[PerfStage.PARTIAL_CONTENT_LOAD.toString()]
        val oldFullContent = oldStatus.durations[PerfStage.FULL_CONTENT_LOAD.toString()]

        updateDurationsMap(
            viewCreated,
            newDurations,
            duration,
            oldViewCreated,
            PerfStage.VIEW_CREATED
        )
        updateDurationsMap(
            titleLoaded,
            newDurations,
            duration,
            oldTitleLoaded,
            PerfStage.TITLE_LOADED
        )
        updateDurationsMap(
            transitionStarted,
            newDurations,
            duration,
            oldTransitionStarted,
            PerfStage.TRANSITION_START
        )
        updateDurationsMap(
            partialContentLoaded,
            newDurations,
            duration,
            oldPartialContent,
            PerfStage.PARTIAL_CONTENT_LOAD
        )
        updateDurationsMap(
            fullContentLoaded,
            newDurations,
            duration,
            oldFullContent,
            PerfStage.FULL_CONTENT_LOAD
        )

        return newDurations
    }

    private fun updateDurationsMap(
        viewCreated: Boolean?,
        newDurations: HashMap<String, Long>,
        duration: Long,
        oldViewCreated: Long?,
        perfStage: PerfStage
    ) {
        if (viewCreated != null) {
            newDurations[perfStage.toString()] = duration
        } else if (oldViewCreated != null) {
            newDurations[perfStage.toString()] = oldViewCreated
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
