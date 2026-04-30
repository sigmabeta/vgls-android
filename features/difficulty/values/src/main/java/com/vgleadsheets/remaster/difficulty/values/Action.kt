package com.vgleadsheets.remaster.difficulty.values

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class DifficultyValueClicked(val id: Long) : Action()
}
