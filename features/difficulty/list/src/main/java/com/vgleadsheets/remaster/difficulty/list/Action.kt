package com.vgleadsheets.remaster.difficulty.list

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class DifficultyTypeClicked(val id: Long) : Action()
}
