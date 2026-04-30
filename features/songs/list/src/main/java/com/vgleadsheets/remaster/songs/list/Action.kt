package com.vgleadsheets.remaster.songs.list

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class SongClicked(val id: Long) : Action()
}
