package com.vgleadsheets.remaster.games.list

import com.vgleadsheets.list.ListAction

sealed class Action : ListAction() {
    data class GameClicked(val id: Long) : Action()
}
