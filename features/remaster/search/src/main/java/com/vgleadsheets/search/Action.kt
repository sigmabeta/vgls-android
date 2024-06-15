package com.vgleadsheets.search

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class SongClicked(val id: Long) : Action()
    data class GameClicked(val id: Long) : Action()
    data class ComposerClicked(val id: Long) : Action()
}
