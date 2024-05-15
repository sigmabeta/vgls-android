package com.vgleadsheets.remaster.songs.list

import com.vgleadsheets.state.VglsAction

sealed class Action : VglsAction() {
    data class SongClicked(val id: Long) : Action()
}
