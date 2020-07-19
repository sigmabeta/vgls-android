package com.vgleadsheets.perf.view.common

data class PerfViewStatus(
    val screenStatuses: List<PerfViewScreenStatus> = emptyList()
) {
    fun getScreenByName(screenName: String) = screenStatuses
        .firstOrNull { it.screenName == screenName }
}