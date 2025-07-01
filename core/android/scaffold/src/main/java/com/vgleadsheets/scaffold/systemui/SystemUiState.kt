package com.vgleadsheets.scaffold.systemui

import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.nav.SystemUiVisibility

data class SystemUiState(
    val currentDestination: String = Destination.NONE.name,
    val visibility: SystemUiVisibility = SystemUiVisibility.VISIBLE
) : VglsState {
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
