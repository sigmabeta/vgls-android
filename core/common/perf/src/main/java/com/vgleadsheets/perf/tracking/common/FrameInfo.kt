package com.vgleadsheets.perf.tracking.common

data class FrameInfo(
    val startTimeNanos: Long,
    val durationOnUiThreadNanos: Long,
    val isJank: Boolean
)
