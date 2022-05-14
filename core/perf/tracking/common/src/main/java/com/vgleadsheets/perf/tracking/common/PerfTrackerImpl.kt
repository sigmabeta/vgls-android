package com.vgleadsheets.perf.tracking.common

import com.vgleadsheets.perf.tracking.api.PerfScreenStatus
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.api.PerfState
import com.vgleadsheets.perf.tracking.api.PerfTracker
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

@SuppressWarnings("TooManyFunctions")
class PerfTrackerImpl(private val perfTrackingBackend: PerfTrackingBackend) : PerfTracker {
    private val screens =
        HashMap<PerfSpec, PerfScreenStatus>()

    private val failureTimers =
        HashMap<PerfSpec, Disposable?>()

    private val eventSink = BehaviorSubject.create<PerfState>()

    override fun getEventStream() = eventSink

    override fun start(screenName: String, spec: PerfSpec) {
        val oldScreen = screens[spec]
        if (oldScreen != null) {
            if (
                oldScreen.stageDurations[PerfStage.CANCELLATION] != null &&
                oldScreen.stageDurations[PerfStage.COMPLETION] != null
            ) {
                perfTrackingBackend.error(
                    "Active trace list from a previous instance of screen $screenName still exists!"
                )
                return
            }
        }

        perfTrackingBackend.startScreen(screenName)
        val startTime = System.currentTimeMillis()

        Timber.d("Starting timing for $screenName...")

        publishState()

        screens[spec] = PerfScreenStatus(screenName, startTime)
        startFailureTimer(spec)
    }

    override fun onViewCreated(spec: PerfSpec) =
        finishTrace(spec, PerfStage.VIEW_CREATED)

    override fun onTitleLoaded(spec: PerfSpec) =
        finishTrace(spec, PerfStage.TITLE_LOADED)

    override fun onTransitionStarted(spec: PerfSpec) =
        finishTrace(spec, PerfStage.TRANSITION_START)

    override fun onPartialContentLoad(spec: PerfSpec) =
        finishTrace(spec, PerfStage.PARTIAL_CONTENT_LOAD)

    override fun onFullContentLoad(spec: PerfSpec) =
        finishTrace(spec, PerfStage.FULL_CONTENT_LOAD)

    @SuppressWarnings("ReturnCount")
    override fun cancel(spec: PerfSpec) {
        val screen = screens[spec] ?: return

        cancelInternal(screen, spec)
    }

    override fun cancelAll() {
        Timber.w("Canceling all screen timers.")

        screens.forEach { entry ->
            cancelInternal(entry.value, entry.key)
        }
    }

    @SuppressWarnings("ReturnCount")
    override fun clear(spec: PerfSpec) {
        val screen = screens[spec]

        if (screen == null) {
            perfTrackingBackend.error("Screen $spec not found!")
            return
        }

        if (screen.stageDurations[PerfStage.CANCELLATION] != null) {
            return
        }

        if (screen.stageDurations[PerfStage.COMPLETION] != null) {
            return
        }

        Timber.i("Clearing ${screen.name} from traces list.")

        val timer = failureTimers[spec]
        if (timer != null) {
            Timber.w("Timer for ${screen.name} was not previously cleared.")
            stopFailureTimer(spec)
        }

        screens.remove(spec)
    }

    private fun publishState() {
        // Timber.i("Publishing trace.")
        eventSink.onNext(
            PerfState(
                screens.toMap()
            )
        )
    }

    private fun cancelInternal(screen: PerfScreenStatus, spec: PerfSpec) {
        if (screen.stageDurations[PerfStage.CANCELLATION] != null) {
            return
        }

        if (screen.stageDurations[PerfStage.COMPLETION] != null) {
            return
        }

        perfTrackingBackend.cancel(screen.name)
        val duration = System.currentTimeMillis() - screen.startTime

        Timber.i("Cancelling timing for ${screen.name} after $duration ms.")

        screens[spec] = screen.copy(
            stageDurations = screen.stageDurations + (PerfStage.CANCELLATION to duration)
        )

        publishState()

        stopFailureTimer(spec)
    }

    @SuppressWarnings("ReturnCount")
    private fun finishTrace(spec: PerfSpec, perfStage: PerfStage) {
        val screen = screens[spec]
            ?: return

        if (screen.stageDurations[perfStage] != null) {
            return
        }

        if (screen.stageDurations[PerfStage.CANCELLATION] != null) {
            return
        }

        if (screen.stageDurations[PerfStage.COMPLETION] != null) {
            return
        }

        perfTrackingBackend.finishTrace(screen.name, perfStage)

        val duration = System.currentTimeMillis() - screen.startTime
        val updatedScreen = screen.copy(
            stageDurations = screen.stageDurations + (perfStage to duration)
        )

        Timber.v("Duration for ${screen.name}:$perfStage: $duration ms ")

        screens[spec] = updatedScreen
        publishState()

        checkIfScreenFullyLoaded(updatedScreen, spec, duration)
        return
    }

    private fun checkIfScreenFullyLoaded(
        screen: PerfScreenStatus,
        spec: PerfSpec,
        duration: Long
    ) {
        val durations = screen.stageDurations
        if (durations[PerfStage.COMPLETION] != null) {
            return
        }

        val durationsRecorded = durations
            .exceptCancelAndComplete()
            .size

        if (PerfStage.values().exceptCancelAndComplete().size > durationsRecorded) {
            return
        }

        onScreenFullyLoaded(spec, duration)
    }

    private fun onScreenFullyLoaded(spec: PerfSpec, duration: Long) {
        val screen = screens[spec]

        if (screen == null) {
            perfTrackingBackend.error("Traces for $spec not found!")
            return
        }

        if (screen.stageDurations[PerfStage.COMPLETION] != null) {
            return
        }

        Timber.d("Successful load of ${screen.name} in $duration ms!")

        screens[spec] = screen.copy(
            stageDurations = screen.stageDurations + (PerfStage.COMPLETION to duration)
        )
        publishState()

        stopFailureTimer(spec)
    }

    private fun startFailureTimer(spec: PerfSpec) {
        val timer = Observable.timer(TIMEOUT_SCREEN_LOAD, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val screen = screens[spec]
                if (screen == null) {
                    Timber.w("Trace not found when timer went off. What?")
                    return@subscribe
                }

                val notClearedTraces = getNotClearedTraces(screen)

                perfTrackingBackend.error(
                    "Screen ${screen.name} waited $TIMEOUT_SCREEN_LOAD ms, but still missing events for: $notClearedTraces"
                )
            }
        failureTimers[spec] = timer
    }

    private fun stopFailureTimer(spec: PerfSpec) {
        val timer = failureTimers[spec]

        if (timer == null) {
            Timber.w("Failure timer for $spec has already been stopped!")
            return
        }

        timer.dispose()

        failureTimers[spec] = null
    }

    private fun getNotClearedTraces(screen: PerfScreenStatus): List<PerfStage> {
        return PerfStage.values()
            .exceptCancelAndComplete()
            .filter { !screen.stageDurations.containsKey(it) }
    }

    private fun Array<PerfStage>.exceptCancelAndComplete() = this
        .filter {
            it != PerfStage.COMPLETION && it != PerfStage.CANCELLATION
        }

    private fun Map<PerfStage, Long?>.exceptCancelAndComplete() = this
        .filter {
            it.key != PerfStage.COMPLETION && it.key != PerfStage.CANCELLATION
        }

    companion object {
        const val TIMEOUT_SCREEN_LOAD = 30_000L
    }
}
