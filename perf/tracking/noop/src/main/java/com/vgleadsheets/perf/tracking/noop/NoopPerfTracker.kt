package com.vgleadsheets.perf.tracking.noop

import com.vgleadsheets.perf.tracking.common.PerfEvent
import com.vgleadsheets.perf.tracking.common.PerfScreenStatus
import com.vgleadsheets.perf.tracking.common.PerfStage
import com.vgleadsheets.perf.tracking.common.PerfTracker
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class NoopPerfTracker : PerfTracker {
    private val screens =
        HashMap<String, PerfScreenStatus>()

    private val failureTimers =
        HashMap<String, Disposable?>()

    private val eventSink = BehaviorSubject.create<PerfEvent>()

    override fun getEventStream() = eventSink

    override fun start(screenName: String) {
        if (screens[screenName] != null) {
            throw IllegalStateException(
                "Active trace list from a previous instance of screen $screenName " +
                        "still exists!"
            )
        }

        Timber.d("Starting timing for $screenName...")
        eventSink.onNext(
            PerfEvent(
                screenName,
                null,
                -1L
            )
        )

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
        val screen = screens[screenName]
            ?: throw IllegalStateException("Traces for $screenName not found!")

        if (screen.stages[PerfStage.CANCELLATION.ordinal]) {
            return
        }

        val duration = System.currentTimeMillis() - screen.startTime

        Timber.w("Cancelling timing for $screenName after $duration ms.")
        eventSink.onNext(
            PerfEvent(
                screenName,
                PerfStage.CANCELLATION,
                duration
            )
        )
        screen.stages[PerfStage.CANCELLATION.ordinal] = true
    }

    private fun finishTrace(screenName: String, perfStage: PerfStage) {
        val screen = screens[screenName]
            ?: return

        if (screen.stages[perfStage.ordinal]) {
            return
        }

        if (screen.stages[PerfStage.COMPLETION.ordinal]) {
            throw IllegalStateException("Trace $screenName:$perfStage has already been completed!")
        }

        if (screen.stages[PerfStage.CANCELLATION.ordinal]) {
            throw IllegalStateException("Trace $screenName:$perfStage has already been cancelled!")
        }

        val duration = System.currentTimeMillis() - screen.startTime
        Timber.v("Duration for $screenName:$perfStage: $duration ms ")
        eventSink.onNext(
            PerfEvent(
                screenName,
                perfStage,
                duration
            )
        )

        screen.stages[perfStage.ordinal] = true
        checkIfScreenFullyLoaded(screen, screenName, duration)

        return
    }

    private fun checkIfScreenFullyLoaded(
        screen: PerfScreenStatus,
        screenName: String,
        duration: Long
    ) {
        screen.stages
            .exceptCancelAndComplete()
            .map { it.second }
            .forEach { finished ->
                if (!finished) {
                    return
                }
            }

        onScreenFullyLoaded(screenName, duration)
    }

    private fun onScreenFullyLoaded(screenName: String, duration: Long) {
        val screen = screens[screenName]
            ?: throw IllegalStateException("Traces for $screenName not found!")

        if (screen.stages[PerfStage.COMPLETION.ordinal]) {
            return
        }

        Timber.d("Successful load of $screenName in $duration ms!")
        screen.stages[PerfStage.COMPLETION.ordinal] = true
        eventSink.onNext(
            PerfEvent(
                screenName,
                PerfStage.COMPLETION,
                duration
            )
        )

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
            .exceptCancelAndComplete()
            .filter { it.second }
            .map { PerfStage.values()[it.first] }
    }

    private fun List<Boolean>.exceptCancelAndComplete() = this
        .mapIndexed { index, value -> Pair(index, value) }
        .filter {
            it.first != PerfStage.COMPLETION.ordinal && it.first != PerfStage.CANCELLATION.ordinal
        }


    companion object {
        const val TIMEOUT_SCREEN_LOAD = 10_000L
    }
}