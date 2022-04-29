package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.hud.perf.PerfViewStatus
import com.vgleadsheets.model.ApiDigest
import com.vgleadsheets.model.parts.Part
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.storage.BooleanSetting

data class HudState(
    val alwaysShowBack: Boolean = false,
    val menuExpanded: Boolean = false,
    val partsExpanded: Boolean = false,
    val hudVisible: Boolean = true,
    val searchVisible: Boolean = false,
    val readyToShowScreens: Boolean = false,
    val searchQuery: String? = null,
    val perfViewStatus: PerfViewStatus = PerfViewStatus(),
    val selectedPart: Part = Part.C,
    val selectedSong: Song? = null,
    val updatePerfView: Async<BooleanSetting> = Uninitialized,
    val updateTime: Async<Long> = Uninitialized,
    val digest: Async<ApiDigest> = Uninitialized,
    val random: Async<Song> = Uninitialized
) : MvRxState
