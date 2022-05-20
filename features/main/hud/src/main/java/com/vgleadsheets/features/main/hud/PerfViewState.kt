package com.vgleadsheets.features.main.hud

import com.vgleadsheets.perf.tracking.api.PerfSpec

data class PerfViewState(
    val viewMode: PerfViewMode = PerfViewMode.REGULAR,
    val selectedScreen: PerfSpec = PerfSpec.values().first(),
)
