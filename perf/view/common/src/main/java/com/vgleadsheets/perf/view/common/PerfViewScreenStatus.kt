package com.vgleadsheets.perf.view.common

data class PerfViewScreenStatus(
    val screenName: String,
    val startTime: Long = System.currentTimeMillis(),
    val completed: Boolean = false,
    val cancellationDuration: Long? = null,
    val viewCreationDuration: Long? = null,
    val titleLoadDuration: Long? = null,
    val transitionStartDuration: Long? = null,
    val partialContentLoadDuration: Long? = null,
    val fullContentLoadDuration: Long? = null
)
