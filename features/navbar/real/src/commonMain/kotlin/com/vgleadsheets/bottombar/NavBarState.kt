package com.vgleadsheets.bottombar

import com.vgleadsheets.nav.Destination
import net.sigmabeta.sage.appcomm.SageState

data class NavBarState(
    val currentDestination: String = Destination.NONE.name,
    val visibility: NavBarVisibility = NavBarVisibility.VISIBLE,
) : SageState {
    val actualVisibility = if (visibility == NavBarVisibility.VISIBLE) {
        NavBarVisibility.VISIBLE
    } else {
        if (canScreenHideNavBar()) {
            NavBarVisibility.HIDDEN
        } else {
            NavBarVisibility.VISIBLE
        }
    }

    private fun canScreenHideNavBar(): Boolean = currentDestination.contains(Destination.SONG_VIEWER.destName)
}
