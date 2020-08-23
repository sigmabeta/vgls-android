package com.vgleadsheets.features.main.hud.perf

data class PerfViewStatus(
    val screenStatuses: List<PerfViewScreenStatus> = emptyList()
) {
    fun getScreenByName(screenName: String) = screenStatuses
        .firstOrNull { it.screenName == screenName }
}
