package com.vgleadsheets.perf.tracking.common

import com.vgleadsheets.tracking.TrackingScreen

interface PerfTracker {
    fun start(trackingScreen: TrackingScreen)

    fun onViewCreated(trackingScreen: TrackingScreen): Long?

    fun onTitleRendered(trackingScreen: TrackingScreen): Long?

    fun onTransitionStarted(trackingScreen: TrackingScreen): Long?

    fun onTransitionEnded(trackingScreen: TrackingScreen): Long?

    fun onPartialLoad(trackingScreen: TrackingScreen): Long?

    fun onFullLoad(trackingScreen: TrackingScreen): Long?

    fun cancel(trackingScreen: TrackingScreen)
}