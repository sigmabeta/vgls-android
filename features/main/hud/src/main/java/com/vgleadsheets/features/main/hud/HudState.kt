package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.model.ApiDigest
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.perf.tracking.api.FrameInfo
import com.vgleadsheets.perf.tracking.api.PerfSpec
import com.vgleadsheets.perf.tracking.api.ScreenLoadStatus

data class HudState(
    val alwaysShowBack: Boolean = false,
    val mode: HudMode = HudMode.REGULAR,
    val hudVisible: Boolean = true,
    val readyToShowScreens: Boolean = false,
    val searchQuery: String? = null,
    val selectedPart: Part = Part.C,
    val selectedSong: Song? = null,
    val loadTimeLists: Map<PerfSpec, ScreenLoadStatus>? = null,
    val frameTimeLists: Map<PerfSpec, List<FrameInfo>>? = null,
    val perfViewState: PerfViewState = PerfViewState(),
    val updateTime: Async<Long> = Uninitialized,
    val digest: Async<ApiDigest> = Uninitialized,
    val random: Async<Song> = Uninitialized
) : MvRxState
