package com.vgleadsheets.remaster.tags.songs

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class SongClicked(val id: Long) : Action()
}
