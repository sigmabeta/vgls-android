package com.vgleadsheets.perf.tracking.noop

import com.vgleadsheets.perf.tracking.common.PerfStage
import com.vgleadsheets.perf.tracking.common.PerfTracker
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class NoopPerfTracker : PerfTracker {
    private val activeTraces =
        HashMap<String, MutableMap<PerfStage, Long?>?>()

    private val failureTimers =
        HashMap<String, Disposable?>()

    override fun start(screenName: String) {
        // Check if an active trace list exists
        if (activeTraces[screenName] != null) {
            throw IllegalStateException(
                "Active trace list from a previous instance of screen $screenName " +
                        "still exists!"
            )
        }

        // Create a new list of active traces
        val newTraces = HashMap<PerfStage, Long?>(PerfStage.values().size)

        PerfStage.values().forEach { stage ->
            newTraces[stage] = System.currentTimeMillis()
        }

        // Add it to the hashmap
        activeTraces[screenName] = newTraces
        Timber.d("Starting timing for $screenName...")

        startFailureTimer(screenName)
    }

    override fun onViewCreated(screenName: String) =
        finishTrace(screenName, PerfStage.VIEW_CREATED)

    override fun onTitleLoaded(screenName: String) =
        finishTrace(screenName, PerfStage.TITLE_LOADED)

    override fun onTransitionStarted(screenName: String) =
        finishTrace(screenName, PerfStage.TRANSITION_START)

    override fun onPartialContentLoad(screenName: String) =
        finishTrace(screenName, PerfStage.PARTIAL_CONTENT_LOAD)

    override fun onFullContentLoad(screenName: String) =
        finishTrace(screenName, PerfStage.FULL_CONTENT_LOAD)

    override fun cancel(screenName: String) {
        Timber.w("Cancelling timing for $screenName...")
        removeTracesFor(screenName)
    }

    private fun finishTrace(screenName: String, perfStage: PerfStage): Long? {
        val tracesForScreen = activeTraces[screenName]
            ?: return null

        val startTime = tracesForScreen[perfStage]
            ?: throw IllegalStateException("Trace $screenName:$perfStage has already been stopped!")

        tracesForScreen[perfStage] = null

        val duration = System.currentTimeMillis() - startTime
        Timber.v("Duration for $screenName:$perfStage: $duration ms ")

        checkIfScreenFullyLoaded(tracesForScreen, screenName, duration)

        return duration
    }

    private fun checkIfScreenFullyLoaded(
        tracesForScreen: MutableMap<PerfStage, Long?>,
        screenName: String,
        duration: Long
    ) {
        val remainingTraces = getNotClearedTraces(tracesForScreen)
        if (remainingTraces.isEmpty()) {
            onScreenFullyLoaded(screenName, duration)
        }
    }

    private fun onScreenFullyLoaded(screenName: String, duration: Long) {
        Timber.d("Successful load of $screenName in $duration ms!")
        activeTraces[screenName] = null

        stopFailureTimer(screenName)
    }

    private fun removeTracesFor(screenName: String) {
        val tracesForThisScreen = activeTraces[screenName] ?: return

        val notClearedTraces = getNotClearedTraces(tracesForThisScreen)

        if (notClearedTraces.isNotEmpty()) {
            val emptinessMessage = "Missing traces for $screenName: ${notClearedTraces.joinToString()}."

            Timber.i(emptinessMessage)
        }

        activeTraces[screenName] = null

        stopFailureTimer(screenName)
    }

    private fun startFailureTimer(screenName: String) {
        val timer = Observable.timer(TIMEOUT_SCREEN_LOAD, TimeUnit.MILLISECONDS)
            .subscribe {
                val tracesForThisScreen = activeTraces[screenName]
                if (tracesForThisScreen == null) {
                    Timber.w("Timer went off even though traces have been cleared. What?")
                    return@subscribe
                }

                val notClearedTraces = getNotClearedTraces(tracesForThisScreen)

                throw TimeoutException(
                    "Screen $screenName waited $TIMEOUT_SCREEN_LOAD ms, but still missing events for: $notClearedTraces"
                )
            }
        failureTimers[screenName] = timer
    }

    private fun stopFailureTimer(screenName: String) {
        val timer = failureTimers[screenName]
            ?: throw IllegalStateException("Failure timer for $screenName has already been stopped!")

        timer.dispose()

        failureTimers[screenName] = null
    }

    private fun getNotClearedTraces(traces: MutableMap<PerfStage, Long?>): List<PerfStage> {
        return traces
            .entries
            .map { if (it.value == null) null else it }
            .filterNotNull()
            .map { it.key }
            .sortedBy { it.ordinal }
    }

    companion object {
        const val TIMEOUT_SCREEN_LOAD = 10_000L
    }
}