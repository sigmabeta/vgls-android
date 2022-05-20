package com.vgleadsheets.perf.tracking.api

data class FrameTimeStats(
    val jankFrames: Int,
    val totalFrames: Int,
    val medianMillis: Long,
    val ninetyFiveMillis: Long,
    val ninetyNineMillis: Long
)
