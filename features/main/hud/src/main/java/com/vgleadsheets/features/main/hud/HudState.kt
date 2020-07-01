package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.ApiDigest
import com.vgleadsheets.model.song.Song

data class HudState(
    val alwaysShowBack: Boolean = false,
    val menuExpanded: Boolean = false,
    val hudVisible: Boolean = true,
    val searchVisible: Boolean = false,
    val readyToShowScreens: Boolean = false,
    val searchQuery: String? = null,
    val parts: List<PartSelectorItem> = PartSelectorItem.getDefaultPartPickerItems(null),
    val updateTime: Async<Long> = Uninitialized,
    val digest: Async<ApiDigest> = Uninitialized,
    val random: Async<Song> = Uninitialized
) : MvRxState
