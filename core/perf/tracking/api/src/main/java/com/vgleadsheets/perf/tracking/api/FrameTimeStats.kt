package com.vgleadsheets.perf.tracking.api

data class FrameTimeStats(
    val jankFrames: Int,
    val totalFrames: Int,
    val median: Long,
    val ninetyFive: Long,
    val ninetyNine: Long
)

