package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.state.VglsAction

sealed class Action : VglsAction() {
    data class GameClicked(val id: Long) : Action()
}
