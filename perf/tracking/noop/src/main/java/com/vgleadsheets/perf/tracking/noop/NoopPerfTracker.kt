package com.vgleadsheets.perf.tracking.noop

import com.vgleadsheets.perf.tracking.common.PerfScreenStatus
import com.vgleadsheets.perf.tracking.common.PerfStage
import com.vgleadsheets.perf.tracking.common.PerfTracker
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class NoopPerfTracker : PerfTracker {
    private val screens =
        HashMap<String, PerfScreenStatus>()

    private val failureTimers =
        HashMap<String, Disposable?>()

    override fun start(screenName: String) {
        if (screens[screenName] != null) {
            throw IllegalStateException(
                "Active trace list from a previous instance of screen $screenName " +
                        "still exists!"
            )
        }

        Timber.d("Starting timing for $screenName...")

        screens[screenName] = PerfScreenStatus()
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
        val perfScreenStatus = screens[screenName]
            ?: throw IllegalStateException("Traces for $screenName not found!")

        if (perfScreenStatus.cancelled) {
            return
        }

        Timber.w("Cancelling timing for $screenName...")
        perfScreenStatus.cancelled = true
    }

    private fun finishTrace(screenName: String, perfStage: PerfStage) {
        val screen = screens[screenName]
            ?: return

        if (screen.stages[perfStage.ordinal]) {
            return
        }

        if (screen.completed) {
            throw IllegalStateException("Trace $screenName:$perfStage has already been completed!")
        }

        if (screen.cancelled) {
            throw IllegalStateException("Trace $screenName:$perfStage has already been cancelled!")
        }

        val duration = System.currentTimeMillis() - screen.startTime
        Timber.v("Duration for $screenName:$perfStage: $duration ms ")

        screen.stages[perfStage.ordinal] = true
        checkIfScreenFullyLoaded(screen, screenName, duration)

        return
    }

    private fun checkIfScreenFullyLoaded(
        screen: PerfScreenStatus,
        screenName: String,
        duration: Long
    ) {
        screen.stages.forEach { finished ->
            if (!finished) {
                return
            }
        }

        onScreenFullyLoaded(screenName, duration)
    }

    private fun onScreenFullyLoaded(screenName: String, duration: Long) {
        val perfScreenStatus = screens[screenName]
            ?: throw IllegalStateException("Traces for $screenName not found!")

        if (perfScreenStatus.completed) {
            return
        }

        Timber.d("Successful load of $screenName in $duration ms!")
        perfScreenStatus.completed = true

        stopFailureTimer(screenName)
    }

    private fun startFailureTimer(screenName: String) {
        val timer = Observable.timer(TIMEOUT_SCREEN_LOAD, TimeUnit.MILLISECONDS)
            .subscribe {
                val screen = screens[screenName]
                if (screen == null) {
                    Timber.w("Trace not found when timer went off. What?")
                    return@subscribe
                }

                val notClearedTraces = getNotClearedTraces(screen)

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

    private fun getNotClearedTraces(screen: PerfScreenStatus): List<PerfStage> {
        return screen
            .stages
            .mapIndexed { index, value -> Pair(index, value)}
            .filter { it.second }
            .map { PerfStage.values()[it.first] }
    }

    companion object {
        const val TIMEOUT_SCREEN_LOAD = 10_000L
    }
}