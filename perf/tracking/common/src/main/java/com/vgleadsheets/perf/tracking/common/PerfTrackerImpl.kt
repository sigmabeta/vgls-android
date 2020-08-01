package com.vgleadsheets.perf.tracking.common

import com.vgleadsheets.perf.tracking.api.PerfEvent
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.api.PerfTracker
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@SuppressWarnings("TooManyFunctions")
class PerfTrackerImpl(private val perfTrackingBackend: PerfTrackingBackend) : PerfTracker {
    private val screens =
        HashMap<String, PerfScreenStatus>()

    private val failureTimers =
        HashMap<String, Disposable?>()

    private val eventSink = BehaviorSubject.create<PerfEvent>()

    override fun getEventStream() = eventSink

    override fun start(screenName: String, targetTimes: Map<String, Long>?) {
        val oldScreen = screens[screenName]
        if (oldScreen != null) {
            if (
                !oldScreen.stages[PerfStage.CANCELLATION.ordinal] && !oldScreen.stages[PerfStage.COMPLETION.ordinal]
            ) {
                perfTrackingBackend.error(
                    "Active trace list from a previous instance of screen $screenName " +
                            "still exists!"
                )
                return
            }
        }

        val startTime = perfTrackingBackend.startScreen(screenName)

        Timber.d("Starting timing for $screenName...")
        eventSink.onNext(
            PerfEvent(
                screenName,
                null,
                -1L,
                targetTimes
            )
        )

        screens[screenName] = PerfScreenStatus(startTime)
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

    @SuppressWarnings("ReturnCount")
    override fun cancel(screenName: String) {
        val screen = screens[screenName] ?: return

        if (screen.stages[PerfStage.CANCELLATION.ordinal]) {
            return
        }

        if (screen.stages[PerfStage.COMPLETION.ordinal]) {
            return
        }

        perfTrackingBackend.cancel(screenName)
        val duration = System.currentTimeMillis() - screen.startTime

        Timber.i("Cancelling timing for $screenName after $duration ms.")
        eventSink.onNext(
            PerfEvent(
                screenName,
                PerfStage.CANCELLATION,
                duration
            )
        )
        screen.stages[PerfStage.CANCELLATION.ordinal] = true

        stopFailureTimer(screenName)
    }

    @SuppressWarnings("ReturnCount")
    override fun clear(screenName: String) {
        val screen = screens[screenName]

        if (screen == null) {
            perfTrackingBackend.error("Screen $screenName not found!")
            return
        }

        if (screen.stages[PerfStage.CANCELLATION.ordinal]) {
            return
        }

        if (screen.stages[PerfStage.COMPLETION.ordinal]) {
            return
        }

        Timber.i("Clearing $screenName from traces list.")

        val timer = failureTimers[screenName]
        if (timer != null) {
            Timber.w("Timer for $screenName was not previously cleared.")
            stopFailureTimer(screenName)
        }

        screens.remove(screenName)
    }

    @SuppressWarnings("ReturnCount")
    private fun finishTrace(screenName: String, perfStage: PerfStage) {
        val screen = screens[screenName]
            ?: return

        if (screen.stages[perfStage.ordinal]) {
            return
        }

        if (screen.stages[PerfStage.CANCELLATION.ordinal]) {
            return
        }

        if (screen.stages[PerfStage.COMPLETION.ordinal]) {
            return
        }

        val duration = perfTrackingBackend.finishTrace(screenName, perfStage)

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

        if (screen == null) {
            perfTrackingBackend.error("Traces for $screenName not found!")
            return
        }

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

                perfTrackingBackend.error(
                    "Screen $screenName waited $TIMEOUT_SCREEN_LOAD ms, but still missing events for: $notClearedTraces"
                )
            }
        failureTimers[screenName] = timer
    }

    private fun stopFailureTimer(screenName: String) {
        val timer = failureTimers[screenName]

        if (timer == null) {
            Timber.w("Failure timer for $screenName has already been stopped!")
            return
        }

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
        const val TIMEOUT_SCREEN_LOAD = 15_000L
    }
}
