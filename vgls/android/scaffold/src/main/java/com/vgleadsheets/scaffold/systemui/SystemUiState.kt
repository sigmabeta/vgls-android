package com.vgleadsheets.scaffold.systemui

import com.vgleadsheets.nav.SystemUiVisibility
import net.sigmabeta.sage.appcomm.SageState
import net.sigmabeta.sage.nav.Destination

data class SystemUiState(
    val currentDestination: String = Destination.NONE.name,
    val visibility: SystemUiVisibility = SystemUiVisibility.VISIBLE
) : SageState {
    val actualVisibility = if (visibility == SystemUiVisibility.VISIBLE) {
        SystemUiVisibility.VISIBLE
    } else {
        if (canScreenHideSystemUi()) {
            SystemUiVisibility.HIDDEN
        } else {
            SystemUiVisibility.VISIBLE
        }
    }

    private fun canScreenHideSystemUi(): Boolean = currentDestination.contains(Destination.SONG_VIEWER.destName)
}
