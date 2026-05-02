package com.vgleadsheets.remaster.difficulty.values

import net.sigmabeta.sage.appcomm.SageAction

sealed class Action : SageAction() {
    data class DifficultyValueClicked(val id: Long) : Action()
}
