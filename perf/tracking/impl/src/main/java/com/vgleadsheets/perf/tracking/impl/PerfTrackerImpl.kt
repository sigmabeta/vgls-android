package com.vgleadsheets.perf.tracking.impl

import com.google.firebase.perf.FirebasePerformance
import com.vgleadsheets.tracking.Tracker

class PerfTrackerImpl(
    private val firebase: FirebasePerformance,
    private val tracker: Tracker
)