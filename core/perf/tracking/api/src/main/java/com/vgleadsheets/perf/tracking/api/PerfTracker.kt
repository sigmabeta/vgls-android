package com.vgleadsheets.perf.tracking.api

import io.reactivex.Observable

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

    fun screenLoadStream(): Observable<Map<PerfSpec, ScreenLoadStatus>>

    fun frameTimeStream(): Observable<Map<PerfSpec, List<FrameInfo>>>

    fun requestFrameTimeList()
}

