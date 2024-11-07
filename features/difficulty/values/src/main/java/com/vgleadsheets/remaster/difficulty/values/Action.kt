package com.vgleadsheets.remaster.difficulty.values

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class DifficultyValueClicked(val id: Long) : Action()
}
