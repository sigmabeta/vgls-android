package com.vgleadsheets.perf.tracking.common

import com.vgleadsheets.perf.tracking.api.FrameInfo
import com.vgleadsheets.perf.tracking.api.FrameTimeStats
import com.vgleadsheets.perf.tracking.api.InvalidateStats
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.perf.tracking.api.ScreenLoadStatus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import timber.log.Timber

@SuppressWarnings("TooManyFunctions")
class PerfTrackerImpl(private val perfTrackingBackend: PerfTrackingBackend) : PerfTracker {
    private val loadTimeScreens = HashMap<PerfSpec, ScreenLoadStatus>()

    private val invalidateScreens = HashMap<PerfSpec, MutableList<Long>>()

    private val frameTimeScreens = HashMap<PerfSpec, MutableList<FrameInfo>>()

    private val failureTimers = HashMap<PerfSpec, Disposable?>()

    private val screenLoadSink = BehaviorSubject.create<Map<PerfSpec, ScreenLoadStatus>>()

    private val frameTimeSink = BehaviorSubject.create<Map<PerfSpec, FrameTimeStats>>()

    private val invalidateSink = BehaviorSubject.create<Map<PerfSpec, InvalidateStats>>()

    override fun screenLoadStream() = screenLoadSink

    override fun frameTimeStream() = frameTimeSink

    override fun invalidateStream() = invalidateSink

    override fun start(screenName: String, spec: PerfSpec) {
        val oldScreen = loadTimeScreens[spec]
        if (oldScreen != null) {
            if (
                oldScreen.stageDurations[PerfStage.CANCELLATION] != null &&
                oldScreen.stageDurations[PerfStage.COMPLETION] != null
            ) {
                clear(spec)
            }
        }

        perfTrackingBackend.startScreen(screenName)
        val startTime = System.currentTimeMillis()

        Timber.d("Starting timing for $screenName...")

        publishScreenLoads()

        frameTimeScreens[spec] = mutableListOf()
        invalidateScreens[spec] = mutableListOf()
        loadTimeScreens[spec] = ScreenLoadStatus(screenName, startTime)
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
        val screen = loadTimeScreens[spec] ?: return

        cancelInternal(screen, spec)
    }

    override fun cancelAll() {
        Timber.w("Canceling all screen timers.")

        loadTimeScreens.forEach { entry ->
            cancelInternal(entry.value, entry.key)
        }
    }

    override fun reportFrame(frame: FrameInfo, spec: PerfSpec) {
        val frameList = frameTimeScreens[spec]
        frameList?.add(frame)
    }

    override fun reportInvalidate(invalidateTimeMs: Long, spec: PerfSpec) {
        val invalidateList = invalidateScreens[spec]
        invalidateList?.add(invalidateTimeMs)
    }

    override fun requestUpdates() {
        publishFrameTimes()
        publishInvalidates()
    }

    private fun publishScreenLoads() {
        screenLoadSink.onNext(
            loadTimeScreens.toMap()
        )
    }

    private fun publishFrameTimes() {
        frameTimeSink.onNext(
            frameTimeScreens.mapValues {
                val frametimes = it.value.sortedBy { it.durationOnUiThreadMillis }
                val totalFrames = frametimes.size

                try {
                    FrameTimeStats(
                        frametimes.filter { it.isJank }.size,
                        totalFrames,
                        frametimes[totalFrames / 2].durationOnUiThreadMillis,
                        frametimes[totalFrames * 95 / 100].durationOnUiThreadMillis,
                        frametimes[totalFrames * 99 / 100].durationOnUiThreadMillis,
                    )
                } catch (ex: IndexOutOfBoundsException) {
                    FrameTimeStats(
                        -1,
                        -1,
                        -1,
                        -1,
                        -1,
                    )
                }
            }
        )
    }

    private fun publishInvalidates() {
        invalidateSink.onNext(
            invalidateScreens.mapValues {
                val invalidates = it.value.sortedBy { it }
                val totalInvalidates = invalidates.size

                try {
                    InvalidateStats(
                        invalidates.filter { it > 4L }.size,
                        totalInvalidates,
                        invalidates[totalInvalidates / 2],
                        invalidates[totalInvalidates * 95 / 100],
                        invalidates[totalInvalidates * 99 / 100],
                    )
                } catch (ex: IndexOutOfBoundsException) {
                    InvalidateStats(
                        -1,
                        -1,
                        -1,
                        -1,
                        -1,
                    )
                }
            }
        )
    }

    private fun cancelInternal(screen: ScreenLoadStatus, spec: PerfSpec) {
        if (screen.stageDurations[PerfStage.CANCELLATION] != null) {
            return
        }

        if (screen.stageDurations[PerfStage.COMPLETION] != null) {
            return
        }

        perfTrackingBackend.cancel(screen.name)
        val duration = System.currentTimeMillis() - screen.startTime

        Timber.i("Cancelling timing for ${screen.name} after $duration ms.")

        loadTimeScreens[spec] = screen.copy(
            stageDurations = screen.stageDurations + (PerfStage.CANCELLATION to duration)
        )

        publishScreenLoads()

        stopFailureTimer(spec)
    }

    @SuppressWarnings("ReturnCount")
    private fun clear(spec: PerfSpec) {
        val screen = loadTimeScreens[spec]

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

        frameTimeScreens.remove(spec)
        loadTimeScreens.remove(spec)
    }

    @SuppressWarnings("ReturnCount")
    private fun finishTrace(spec: PerfSpec, perfStage: PerfStage) {
        val screen = loadTimeScreens[spec]
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

        loadTimeScreens[spec] = updatedScreen
        publishScreenLoads()

        checkIfScreenFullyLoaded(updatedScreen, spec, duration)
        return
    }

    private fun checkIfScreenFullyLoaded(
        screen: ScreenLoadStatus,
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
        val screen = loadTimeScreens[spec]

        if (screen == null) {
            perfTrackingBackend.error("Traces for $spec not found!")
            return
        }

        if (screen.stageDurations[PerfStage.COMPLETION] != null) {
            return
        }

        Timber.d("Successful load of ${screen.name} in $duration ms!")

        loadTimeScreens[spec] = screen.copy(
            stageDurations = screen.stageDurations + (PerfStage.COMPLETION to duration)
        )
        publishScreenLoads()

        stopFailureTimer(spec)
    }

    private fun startFailureTimer(spec: PerfSpec) {
        val timer = Observable.timer(TIMEOUT_SCREEN_LOAD, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val screen = loadTimeScreens[spec]
                if (screen == null) {
                    Timber.w("Trace not found when timer went off. What?")
                    return@subscribe
                }

                val notClearedTraces = getNotClearedTraces(screen)

                perfTrackingBackend.error(
                    "Screen ${screen.name} waited $TIMEOUT_SCREEN_LOAD ms, " +
                        "but still missing events for: $notClearedTraces"
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

    private fun getNotClearedTraces(screen: ScreenLoadStatus): List<PerfStage> {
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
