package com.vgleadsheets.remaster.songs.list

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class SongClicked(val id: Long) : Action()
}
