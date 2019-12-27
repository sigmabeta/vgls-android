package com.vgleadsheets.features.main.hud

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.song.Song

data class HudState(
    val alwaysShowBack: Boolean = false,
    val menuExpanded: Boolean = false,
    val hudVisible: Boolean = true,
    val searchVisible: Boolean = false,
    val searchQuery: String? = null,
    val parts: List<PartSelectorItem>? = null,
    val updateTime: Async<Long> = Uninitialized,
    val digest: Async<List<VglsApiGame>> = Uninitialized,
    val random: Async<Song> = Uninitialized,
    val activeJamId: Long? = null,
    val activeJamSheetId: Long? = null,
    val jamCancellationReason: String? = null
) : MvRxState
