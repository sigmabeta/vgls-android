package com.vgleadsheets.viewmodel.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.analytics.AnalyticsScreenId
import net.sigmabeta.sage.analytics.isInitAction
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.EventSink
import net.sigmabeta.sage.appcomm.SageAction
import net.sigmabeta.sage.appcomm.SageEvent
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.list.DelayManager
import net.sigmabeta.sage.list.ListState
import net.sigmabeta.sage.list.ListStateActual
import net.sigmabeta.sage.list.SageScheduler
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.ui.StringProvider

/**
 * Plain-ViewModel base for VGLS list/grid screens. Replaces SAGE's `ListViewModelBrain` + the generic
 * `ListViewModel` shell + `BrainProvider`/`FeatureDirectory`: each screen is now its own
 * `@ContributesIntoMap` ViewModel extending this and resolved via `metroViewModel` /
 * `assistedMetroViewModel`. Mirrors Chipbox's `ChipboxListViewModel`, but keeps VGLS's `SageEvent` +
 * `EventDispatcher` wiring (AndroidX nav stays until the Voyager phase) and the analytics / scheduler /
 * `runInBackground` helpers the brains relied on, so brain bodies port over with only the class header,
 * constructor, and an `init` (send init action) changing.
 *
 * Lives in a JVM module (androidx.lifecycle:lifecycle-viewmodel is multiplatform) so the JVM feature
 * modules can extend it directly.
 */
abstract class VglsListViewModel<StateType : ListState> :
    ViewModel(),
    ActionSink,
    EventSink {
    protected abstract val stringProvider: StringProvider
    protected abstract val hatchet: Hatchet
    protected abstract val analytics: Analytics
    protected abstract val dispatchers: SageDispatchers
    protected abstract val delayManager: DelayManager
    protected abstract val eventDispatcher: EventDispatcher
    protected abstract val showDebugProvider: ShowDebugProvider

    val scheduler: SageScheduler by lazy {
        object : SageScheduler {
            override val dispatchers = this@VglsListViewModel.dispatchers
            override val coroutineScope = viewModelScope
            override val delayManager = this@VglsListViewModel.delayManager
        }
    }

    val showDebug by lazy { showDebugProvider.showDebugFlow }

    abstract val screenIdentifier: AnalyticsScreenId?

    abstract fun initialState(): StateType

    protected val internalUiState by lazy { MutableStateFlow(initialState()) }

    /** Rendered state ([ListStateActual]) derived from [internalUiState] via `toActual` on each change. */
    val uiStateActual: StateFlow<ListStateActual> by lazy {
        internalUiState
            .map { it.toActual(stringProvider) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = initialState().toActual(stringProvider),
            )
    }

    protected val internalUiEvents = MutableSharedFlow<SageEvent>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    private val uiEventsJob = internalUiEvents
        .onEach { eventDispatcher.sendEvent(it) }
        .launchIn(viewModelScope)

    protected abstract fun handleAction(action: SageAction)

    protected open fun handleEvent(event: SageEvent) {}

    override fun sendAction(action: SageAction) {
        if (screenIdentifier != null) {
            if (action.isInitAction()) {
                analytics.logScreenView(action, screenIdentifier!!)
            } else {
                analytics.logAction(action, screenIdentifier!!)
            }
        }

        when (action) {
            is SageAction.DeviceBack -> {
                emitEvent(SageEvent.NavigateBack(this.javaClass.simpleName))
                return
            }
            is SageAction.Resume -> {
                eventDispatcher.addEventSink(this)
                emitEvent(SageEvent.ShowUiChrome)
                val titleModel = internalUiState.value.title(stringProvider)
                titleModel.title?.let { title ->
                    emitEvent(
                        SageEvent.UpdateTitle(
                            title = title,
                            subtitle = titleModel.subtitle,
                            shouldShowBack = titleModel.shouldShowBack,
                            source = this.javaClass.simpleName,
                        )
                    )
                }
            }
            is SageAction.Pause -> eventDispatcher.removeEventSink(this)
            else -> Unit
        }

        viewModelScope.launch(scheduler.dispatchers.main) {
            handleAction(action)
        }
    }

    override fun sendEvent(event: SageEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            handleEvent(event)
        }
    }

    override fun onCleared() {
        eventDispatcher.removeEventSink(this)
    }

    protected fun emitEvent(event: SageEvent) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            hatchet.d("Emitting event: $event")
            internalUiEvents.tryEmit(event)
        }
    }

    protected fun updateState(updater: (StateType) -> StateType) {
        viewModelScope.launch(scheduler.dispatchers.main) {
            internalUiState.value = updater(internalUiState.value)
        }
    }

    protected fun <ListType, ReturnType> Flow<List<ListType>>.mapList(
        mapper: (ListType) -> ReturnType
    ): Flow<List<ReturnType>> = map { list -> list.map(mapper) }

    @Suppress("MagicNumber")
    protected fun <EmissionType> Flow<EmissionType>.runInBackground(
        dispatcher: CoroutineDispatcher = scheduler.dispatchers.disk,
        shouldDelay: Boolean = scheduler.delayManager.shouldDelay(),
    ): Job {
        val possiblyDelayedFlow = if (shouldDelay) this.onStart { delay(5000L) } else this
        return possiblyDelayedFlow.flowOn(dispatcher).launchIn(scheduler.coroutineScope)
    }
}
