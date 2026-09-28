package com.vgleadsheets.remaster.games.list

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class GameClicked(val id: Long) : Action()
}
