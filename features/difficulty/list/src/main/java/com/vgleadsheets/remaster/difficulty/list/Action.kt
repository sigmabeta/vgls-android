package com.vgleadsheets.remaster.difficulty.list

import net.sigmabeta.sage.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class DifficultyTypeClicked(val id: Long) : Action()
}
