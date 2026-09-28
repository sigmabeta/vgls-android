package com.vgleadsheets.remaster.tags.songs

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class SongClicked(val id: Long) : Action()
}
