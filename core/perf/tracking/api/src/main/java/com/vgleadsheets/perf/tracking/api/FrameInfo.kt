package com.vgleadsheets.perf.tracking.api

data class FrameInfo(
    val startTimeMillis: Long,
    val durationOnUiThreadMillis: Long,
    val isJank: Boolean
)
