package com.vgleadsheets.perf.tracking.api

data class FrameInfo(
    val startTimeNanos: Long,
    val durationOnUiThreadNanos: Long,
    val isJank: Boolean
)
