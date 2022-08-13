package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.hud.search.SearchContent
import com.vgleadsheets.model.ApiDigest
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.FrameTimeStats
import com.vgleadsheets.perf.tracking.api.InvalidateStats
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.ScreenLoadStatus

data class HudState(
    val alwaysShowBack: Boolean = false,
    val mode: HudMode = HudMode.REGULAR,
    val hudVisible: Boolean = true,
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
    val digest: Async<ApiDigest> = Uninitialized
) : MvRxState
