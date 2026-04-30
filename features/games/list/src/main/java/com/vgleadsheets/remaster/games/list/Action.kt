package com.vgleadsheets.remaster.games.list

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class GameClicked(val id: Long) : Action()
}
