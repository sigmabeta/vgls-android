package com.vgleadsheets.perf.tracking.common

import kotlinx.coroutines.flow.Flow

interface PerfTracker {
    fun start(screenName: String, spec: PerfSpec)

    fun onViewCreated(spec: PerfSpec)

    fun onTitleLoaded(spec: PerfSpec)

    fun onTransitionStarted(spec: PerfSpec)

    fun onPartialContentLoad(spec: PerfSpec)

    fun onFullContentLoad(spec: PerfSpec)

    fun cancel(spec: PerfSpec)

    fun cancelAll()

    fun reportFrame(frame: FrameInfo, spec: PerfSpec)

    fun reportInvalidate(invalidate: InvalidateInfo, spec: PerfSpec)

    fun screenLoadStream(): Flow<Map<PerfSpec, ScreenLoadStatus>>

    fun frameTimeStream(): Flow<Map<PerfSpec, FrameTimeStats>>

    fun invalidateStream(): Flow<Map<PerfSpec, InvalidateStats>>

    fun requestUpdates()
}
