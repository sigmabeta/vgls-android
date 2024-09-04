package com.vgleadsheets.remaster.difficulty.list

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class DifficultyTypeClicked(val id: Long) : Action()
}
