package com.vgleadsheets.topbar

import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.components.TitleBarModel
import net.sigmabeta.sage.nav.Destination

data class TopBarState(
    val model: TitleBarModel = TitleBarModel(),
    val selectedPart: String? = null,
    val currentDestination: String = Destination.NONE.name,
    val visibility: TopBarVisibility = TopBarVisibility.VISIBLE
) : VglsState {
    val actualVisibility = if (visibility == TopBarVisibility.VISIBLE) {
        TopBarVisibility.VISIBLE
    } else {
        if (canScreenHideTopBar()) {
            TopBarVisibility.HIDDEN
        } else {
            TopBarVisibility.VISIBLE
        }
    }

    private fun canScreenHideTopBar(): Boolean = listOf(
        Destination.SONG_VIEWER.destName,
        Destination.SEARCH.destName
    ).any {
        currentDestination.contains(it)
    }
}
