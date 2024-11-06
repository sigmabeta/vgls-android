package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class GameClicked(val id: Long) : Action()
}
