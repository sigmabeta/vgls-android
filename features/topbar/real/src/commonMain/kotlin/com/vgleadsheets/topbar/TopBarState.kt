package com.vgleadsheets.topbar

import net.sigmabeta.sage.appcomm.SageState
import net.sigmabeta.sage.components.TitleBarModel
import com.vgleadsheets.nav.Destination

data class TopBarState(
    val model: TitleBarModel = TitleBarModel(),
    val selectedPart: String? = null,
    val currentDestination: String = Destination.NONE.name,
    val visibility: TopBarVisibility = TopBarVisibility.VISIBLE
) : SageState {
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
