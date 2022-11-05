package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.hud.search.SearchContent
import com.vgleadsheets.model.Part
import com.vgleadsheets.model.Song
import com.vgleadsheets.perf.tracking.common.FrameTimeStats
import com.vgleadsheets.perf.tracking.common.InvalidateStats
import com.vgleadsheets.perf.tracking.common.PerfSpec
import com.vgleadsheets.perf.tracking.common.ScreenLoadStatus

data class HudState(
    val alwaysShowBack: Boolean = false,
    val mode: HudMode = HudMode.REGULAR,
    val viewerScreenVisible: Boolean = false,
    val searchQuery: String? = null,
    val searchResults: SearchContent = SearchContent(
        Uninitialized,
        Uninitialized,
        Uninitialized,
        false
    ),
    val selectedPart: Part = Part.C,
    val selectedSong: Song? = null,
    val loadTimeLists: Map<PerfSpec, ScreenLoadStatus>? = null,
    val frameTimeStatsMap: Map<PerfSpec, FrameTimeStats>? = null,
    val invalidateStatsMap: Map<PerfSpec, InvalidateStats>? = null,
    val perfViewState: PerfViewState = PerfViewState(),
    val updateTime: Async<Long> = Uninitialized,
    val digest: Async<Unit> = Uninitialized
) : MavericksState
