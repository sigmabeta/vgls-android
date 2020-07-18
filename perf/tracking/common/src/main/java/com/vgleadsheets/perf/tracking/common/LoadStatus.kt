package com.vgleadsheets.perf.tracking.common

data class LoadStatus(
    val titleLoaded: Boolean = false,
    val transitionStarted: Boolean = false,
    val contentPartiallyLoaded: Boolean = false,
    val contentFullyLoaded: Boolean = false,
    val loadFailed: Boolean = false,
    val cancelled: Boolean = false
)