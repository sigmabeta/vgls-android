package com.vgleadsheets.perf.tracking.common

import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.perf.tracking.api.FrameInfo
import com.vgleadsheets.perf.tracking.api.FrameTimeStats
import com.vgleadsheets.perf.tracking.api.InvalidateInfo
import com.vgleadsheets.perf.tracking.api.InvalidateStats
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.PerfStage
import com.vgleadsheets.perf.tracking.api.PerfTracker
import com.vgleadsheets.perf.tracking.api.ScreenLoadStatus
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressWarnings(
    "TooManyFunctions",
    "MagicNumber",
    "TooGenericExceptionCaught",
    "SwallowedException",
    "MaxLineLength"
)
class PerfTrackerImpl(
    private val perfTrackingBackend: PerfTrackingBackend,
    private val dispatchers: VglsDispatchers
) : PerfTracker {
    private val perfCoroutineScope = CoroutineScope(dispatchers.main)

    private val loadTimeScreens = HashMap<PerfSpec, ScreenLoadStatus>()

    private val invalidateScreens = HashMap<PerfSpec, MutableList<InvalidateInfo>>()

    private val frameTimeScreens = HashMap<PerfSpec, MutableList<FrameInfo>>()

    private val failureTimers = HashMap<PerfSpec, Job?>()

    private val screenLoadSink = MutableSharedFlow<Map<PerfSpec, ScreenLoadStatus>>()

    private val frameTimeSink = MutableSharedFlow<Map<PerfSpec, FrameTimeStats>>()

    private val invalidateSink = MutableSharedFlow<Map<PerfSpec, InvalidateStats>>()

    override fun screenLoadStream() = screenLoadSink

    override fun frameTimeStream() = frameTimeSink

    override fun invalidateStream() = invalidateSink

    override fun start(screenName: String, spec: PerfSpec) {
        val oldScreen = loadTimeScreens[spec]
        if (oldScreen != null) {
            if (
                oldScreen.stageDurationMillis[PerfStage.CANCELLATION] != null &&
                oldScreen.stageDurationMillis[PerfStage.COMPLETION] != null
            ) {
                clear(spec)
            }
        }

        perfTrackingBackend.startScreen(screenName)
        val startTimeNanos = System.nanoTime()

        Timber.d("Starting timing for $screenName...")

        publishScreenLoads()

        frameTimeScreens[spec] = mutableListOf()
        invalidateScreens[spec] = mutableListOf()
        loadTimeScreens[spec] = ScreenLoadStatus(screenName, startTimeNanos)
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

    override fun reportInvalidate(invalidate: InvalidateInfo, spec: PerfSpec) {
        val invalidateList = invalidateScreens[spec]
        invalidateList?.add(invalidate)
    }

    override fun requestUpdates() {
        publishFrameTimes()
        publishInvalidates()
    }

    private fun publishScreenLoads() {
        perfCoroutineScope.launch {
            screenLoadSink.emit(
                loadTimeScreens.toMap()
            )
        }
    }

    private fun publishFrameTimes() {
        perfCoroutineScope.launch {
            frameTimeSink.emit(
                frameTimeScreens.mapValues {
                    val frametimes = it.value.sortedBy { it.durationOnUiThreadNanos }
                    val totalFrames = frametimes.size

                    try {
                        FrameTimeStats(
                            frametimes.filter { frame -> frame.isJank }.size,
                            totalFrames,
                            frametimes[totalFrames / 2].durationOnUiThreadNanos.nanoseconds.inWholeMilliseconds,
                            frametimes[totalFrames * 95 / 100].durationOnUiThreadNanos.nanoseconds.inWholeMilliseconds,
                            frametimes[totalFrames * 99 / 100].durationOnUiThreadNanos.nanoseconds.inWholeMilliseconds,
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
    }

    private fun publishInvalidates() {
        perfCoroutineScope.launch {
            invalidateSink.emit(
                invalidateScreens.mapValues {
                    val invalidates = it.value.sortedBy { it.durationNanos }
                    val totalInvalidates = invalidates.size

                    try {
                        InvalidateStats(
                            invalidates.filter { invalidate -> invalidate.durationNanos > 4L.milliseconds.inWholeNanoseconds }.size,
                            totalInvalidates,
                            invalidates.sumOf { invalidate -> invalidate.durationNanos }.nanoseconds.inWholeMilliseconds,
                            invalidates[totalInvalidates / 2].durationNanos.nanoseconds.inWholeMilliseconds,
                            invalidates[totalInvalidates * 95 / 100].durationNanos.nanoseconds.inWholeMilliseconds,
                            invalidates[totalInvalidates * 99 / 100].durationNanos.nanoseconds.inWholeMilliseconds,
                        )
                    } catch (ex: IndexOutOfBoundsException) {
                        InvalidateStats(
                            -1,
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
    }

    private fun cancelInternal(screen: ScreenLoadStatus, spec: PerfSpec) {
        if (screen.stageDurationMillis[PerfStage.CANCELLATION] != null) {
            return
        }

        if (screen.stageDurationMillis[PerfStage.COMPLETION] != null) {
            return
        }

        perfTrackingBackend.cancel(screen.name)
        val durationMillis = (System.nanoTime() - screen.startTimeNanos)
            .nanoseconds
            .inWholeMilliseconds

        Timber.i("Cancelling timing for ${screen.name} after $durationMillis ms.")

        loadTimeScreens[spec] = screen.copy(
            stageDurationMillis = screen.stageDurationMillis + (PerfStage.CANCELLATION to durationMillis)
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

        if (screen.stageDurationMillis[PerfStage.CANCELLATION] != null) {
            return
        }

        if (screen.stageDurationMillis[PerfStage.COMPLETION] != null) {
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

        if (screen.stageDurationMillis[perfStage] != null) {
            return
        }

        if (screen.stageDurationMillis[PerfStage.CANCELLATION] != null) {
            return
        }

        if (screen.stageDurationMillis[PerfStage.COMPLETION] != null) {
            return
        }

        perfTrackingBackend.finishTrace(screen.name, perfStage)

        val durationMillis = (System.nanoTime() - screen.startTimeNanos)
            .nanoseconds
            .inWholeMilliseconds

        val updatedScreen = screen.copy(
            stageDurationMillis = screen.stageDurationMillis + (perfStage to durationMillis)
        )

        Timber.v("Duration for ${screen.name}:$perfStage: $durationMillis ms ")

        synchronized(loadTimeScreens) {
            loadTimeScreens[spec] = updatedScreen
            publishScreenLoads()
        }

        checkIfScreenFullyLoaded(updatedScreen, spec, durationMillis)
        return
    }

    private fun checkIfScreenFullyLoaded(
        screen: ScreenLoadStatus,
        spec: PerfSpec,
        duration: Long
    ) {
        val durations = screen.stageDurationMillis
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

        if (screen.stageDurationMillis[PerfStage.COMPLETION] != null) {
            return
        }

        Timber.d("Successful load of ${screen.name} in $duration ms!")

        loadTimeScreens[spec] = screen.copy(
            stageDurationMillis = screen.stageDurationMillis + (PerfStage.COMPLETION to duration)
        )
        publishScreenLoads()

        stopFailureTimer(spec)
    }

    private fun startFailureTimer(spec: PerfSpec) {
        val timer = perfCoroutineScope.launch {
            delay(TIMEOUT_SCREEN_LOAD)
            val screen = loadTimeScreens[spec]
            if (screen == null) {
                Timber.w("Trace not found when timer went off. What?")
                return@launch
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

        timer.cancel()

        failureTimers[spec] = null
    }

    private fun getNotClearedTraces(screen: ScreenLoadStatus): List<PerfStage> {
        return PerfStage.values()
            .exceptCancelAndComplete()
            .filter { !screen.stageDurationMillis.containsKey(it) }
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
