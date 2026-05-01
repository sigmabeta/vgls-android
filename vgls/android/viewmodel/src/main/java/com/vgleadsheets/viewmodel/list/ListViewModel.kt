package com.vgleadsheets.viewmodel.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import net.sigmabeta.sage.analytics.Analytics
import net.sigmabeta.sage.appcomm.ActionSink
import net.sigmabeta.sage.appcomm.EventDispatcher
import net.sigmabeta.sage.appcomm.EventSink
import net.sigmabeta.sage.appcomm.VglsAction
import net.sigmabeta.sage.appcomm.VglsEvent
import net.sigmabeta.sage.components.ErrorStateListModel
import net.sigmabeta.sage.coroutines.SageDispatchers
import net.sigmabeta.sage.debug.ShowDebugProvider
import net.sigmabeta.sage.list.BrainProvider
import net.sigmabeta.sage.list.ListViewModelBrain
import net.sigmabeta.sage.list.getErrors
import net.sigmabeta.sage.logging.Hatchet
import net.sigmabeta.sage.nav.Destination
import net.sigmabeta.sage.perf.PerfMeasurer

class ListViewModel @AssistedInject constructor(
    brainProvider: BrainProvider,
    private val hatchet: Hatchet,
    private val dispatchers: SageDispatchers,
    private val eventDispatcher: EventDispatcher,
    private val showDebugProvider: ShowDebugProvider,
    private val analytics: Analytics,
    private val perfMeasurer: PerfMeasurer,
    @Assisted destination: Destination,
    @Assisted idArg: Long,
    @Assisted stringArg: String?,
) : ViewModel(),
    ActionSink,
    EventSink {
    private val brain: ListViewModelBrain = brainProvider.provideBrain(
        destination,
        viewModelScope,
    )

    val uiState = brain.uiStateActual

    val showDebug = showDebugProvider.showDebugFlow

    private val reportedErrors = mutableSetOf<ErrorStateListModel>()

    init {
        val initAction = when {
            (idArg > 0L) -> VglsAction.InitWithId(idArg)
            stringArg != null -> VglsAction.InitWithString(stringArg)
            else -> VglsAction.InitNoArgs
        }

        this.sendAction(initAction)

        setupErrorReporting()

        brain.uiEvents
            .onEach { eventDispatcher.sendEvent(it) }
            .launchIn(viewModelScope)
    }

    fun onResume() {
        eventDispatcher.addEventSink(this)
    }

    fun onPause() {
        eventDispatcher.removeEventSink(this)
    }

    override fun sendAction(action: VglsAction) {
        when (action) {
            is VglsAction.Resume -> onResume()
            is VglsAction.Pause -> onPause()
            else -> {}
        }
        brain.sendAction(action)
    }

    override fun sendEvent(event: VglsEvent) = brain.sendEvent(event)

    private fun setupErrorReporting() {
        uiState
            .map { it.getErrors() }
            .onEach { list ->
                list.forEach { reportError(it) }
            }
            .flowOn(dispatchers.computation)
            .launchIn(viewModelScope)
    }

    private fun reportError(errorModel: ErrorStateListModel) {
        if (reportedErrors.contains(errorModel)) {
            return
        }

        reportedErrors.add(errorModel)

        hatchet.e("Error: ${errorModel.failedOperationName} | ${errorModel.errorString}")
        analytics.logError(
            failedOperationName = errorModel.failedOperationName,
            errorString = errorModel.errorString,
            error = errorModel.error
        )
    }
}
