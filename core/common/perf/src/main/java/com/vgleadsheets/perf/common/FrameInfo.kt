package com.vgleadsheets.perf.common

data class FrameInfo(
    val startTimeNanos: Long,
    val durationOnUiThreadNanos: Long,
    val isJank: Boolean
)
