package com.vgleadsheets.remaster.songs.detail

import com.vgleadsheets.state.VglsAction

sealed class Action : VglsAction() {
    data class ComposerClicked(val id: Long) : Action()
    data class GameClicked(val id: Long) : Action()
}
